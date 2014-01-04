package looty
package views

import org.scalajs.jquery.JQueryStatic
import scala.scalajs.js
import looty.model.{ComputedItemProps, PoeCacher, ComputedItem}
import looty.poeapi.PoeTypes.{AnyItem, Leagues}
import looty.model.parsers.ItemParser


//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/9/13 11:17 PM
//////////////////////////////////////////////////////////////

case class LootFilter(text: String, p: ComputedItem => Boolean) {
  def allows(i: ComputedItem): Boolean = {
    try p(i) catch {case e: Throwable => false}
  }
}


class LootView() extends View {
  val jq      : JQueryStatic = global.jQuery.asInstanceOf[JQueryStatic]
  var grid    : js.Dynamic   = null
  var dataView: js.Dynamic   = js.Dynamic.newInstance(global.Slick.Data.DataView)()
  dataView.setIdGetter { (d: ComputedItem) =>
    val id = d.item.getLocationId.toJs
    console.log(id)
    id
  }
  var allItems: js.Array[ComputedItem] = null


  def start() {
    val items = new js.Array[ComputedItem]()
    val pc = new PoeCacher()

    val fut = for {
      tabInfos <- pc.getStashInfo(Leagues.Standard)
      tabs <- pc.getAllStashTabs(Leagues.Standard)
      invs <- pc.getAllInventories(Leagues.Standard)
    } yield {

      //TODO Remove take1
      for {
        (tab, i) <- tabs.zipWithIndex //.take(1)
        item <- tab.allItems(None)
      } {
        val ci = ItemParser.parseItem(item)
        ci.location = tabInfos(i).n
        items.push(ci)
      }

      //TODO Remove take1
      for {
        (char, inv) <- invs //.take(1)
        item <- inv.allItems(Some(char))
      } {
        val ci = ItemParser.parseItem(item)
        ci.location = char
        items.push(ci)
      }

      this.allItems = items

      setHtml()
      showComputedItems(items)
    }

    fut.log()
  }

  def stop() {}

  private def setHtml() {
    val el = jq("#content")
    el.empty()
    el.append("""<div id="controls"></div>""")
    el.append("""<div id="grid"></div>""")
    el.append("""<div id="itemdetail" style="z-index:100;color:white;background-color:black;opacity:.9;display:none;position:fixed;left:50px;top:100px">SAMPLE DATA<br>a<br>a<br>a<br>a<br>a<br>a<br>a<br>a<br>a</div>""")
    appendControls()
    appendGrid()
  }

  private def appendControls() {
    val jq: JQueryStatic = global.jQuery.asInstanceOf[JQueryStatic]
    val el = jq("#controls")
    el.empty()

    val pc = new PoeCacher()

    //Buttons for stashed
    for {
      stis <- pc.Net.getStisAndStore(Leagues.Standard)
      sti <- stis.toList
    } {
      val button = jq(s"""<button style="color: white;text-shadow: -1px 0 black, 0 1px black, 1px 0 black, 0 -1px black;background-color:${sti.colour.toRgb}">${sti.n}</button>""")
      el.append(button)
      button.on("click", (a: js.Any) => {
        //Set the grid to only have this tabs items in it and refresh this tab
        pc.Net.getStashTabAndStore(Leagues.Standard, sti.i.toInt).foreach(st => showAnyItems(st.allItems(None), sti.n))
      })
    }

    //Buttons for characters
    for {
      chars <- pc.Net.getCharsAndStore
      char <- chars.toList
    } {
      val button = jq(s"""<button>${char.name}</button>""")
      el.append(button)
      button.on("click", (a: js.Any) => {
        //Set the grid to only have this tabs items in it and refresh this tab
        pc.Net.getInvAndStore(char.name).foreach(inv => showAnyItems(inv.allItems(Some(char.name)), char.name))
      })
    }

  }

  private def showAnyItems(xs: List[AnyItem], location: String) {
    val items = new js.Array[ComputedItem]()
    for {
      item <- xs
    } {
      val ci = ItemParser.parseItem(item)
      ci.location = location
      items.push(ci)
    }
    showComputedItems(items)
  }

  private def showComputedItems(xs: js.Array[ComputedItem]) {
    console.log("SETTING ITEMS", xs)
    dataView.setItems(xs)
    grid.invalidate()
    grid.render()
  }

  private def appendGrid() {
    def makeColumn(name: String, tooltip: String)(f: ComputedItem => js.Any) = {
      val o = newObject
      o.id = name
      o.name = name
      o.field = name
      o.toolTip = tooltip
      o.sortable = true
      o.getter = f
      o
    }
    val columns = js.Array[js.Dynamic]()
    var columnFilters = Map.empty[String, LootFilter]

    ComputedItemProps.all.foreach { p =>
      val col = makeColumn(p.shortName, p.description)(p.getJs)
      columns.push(col)
    }

    def setFilter(colId: String, text: String) {
      val col = columns.find(_.id == colId.toJs).get
      if (text.trim.isEmpty) {
        columnFilters -= colId
      } else {
        def numFilter(n: String)(p: (Double, Double) => Boolean) = {
          val x = n.toDouble
          LootFilter(text, i => p(col.getter(i.asInstanceOf[js.Any]).toString.toDouble, x))
        }
        val GT = ">(.*)".r
        val GTE = ">=(.*)".r
        val LT = "<(.*)".r
        val LTE = "<=(.*)".r
        val EQ = "=(.*)".r

        val fil = try {
          text.trim match {
            case GTE(n) if (n.nonEmpty) => numFilter(n)(_ >= _)
            case GT(n) if (n.nonEmpty) => numFilter(n)(_ > _)
            case LTE(n) if (n.nonEmpty) => numFilter(n)(_ <= _)
            case LT(n) if (n.nonEmpty) => numFilter(n)(_ < _)
            case EQ(n) if (n.nonEmpty) => numFilter(n)(_ == _)
            case _ =>
              LootFilter(text, i => true)
          }
        } catch {
          case e: Throwable =>
            LootFilter(text, i => true)
        }
        columnFilters += colId -> fil
      }
    }

    val options = {
      val o = newObject
      o.enableCellNavigation = true
      o.enableColumnReorder = false
      o.multiColumnSort = true
      o.showHeaderRow = true
      o.headerRowHeight = 30
      o.explicitInitialization = true
      o.dataItemColumnValueExtractor = (item: ComputedItem, column: js.Dynamic) => {
        column.getter(item.asInstanceOf[js.Any])
      }
      o
    }

    def filter(item: ComputedItem): js.Boolean = {
      columnFilters.forall { case (colId, fil) =>
        fil.allows(item)
      }
    }


    grid = js.Dynamic.newInstance(global.Slick.Grid)("#grid", dataView, columns, options)

    dataView.onRowCountChanged.subscribe(() => {
      grid.updateRowCount()
      grid.render()
    })

    dataView.onRowsChanged.subscribe((e: js.Dynamic, args: js.Dynamic) => {
      grid.invalidateRows(args.rows)
      grid.render()
    })

    dataView.setFilter(filter _)

    grid.onHeaderRowCellRendered.subscribe((e: js.Dynamic, args: js.Dynamic) => {
      jq(args.node).empty()
      val el = jq("<input type='text'>")
          .data("columnId", args.column.id)

      columnFilters.get(args.column.id.asJsStr).foreach { fil =>
        el.`val`(fil.text)
      }

      el.on("keyup", () => {
        setFilter(args.column.id.toString, el.`val`().toString)
        dataView.refresh()
      })
      el.appendTo(args.node)
    })

    addSort()
    addMouseover()
    //addFiltering()

    def resize() {
      jq("#grid").css("height", global.window.innerHeight - 120)
      grid.resizeCanvas()
    }

    jq(global.window).resize(() => resize())
    resize()

    grid.init()

  }

  private def addSort() {
    grid.onSort.subscribe((e: js.Dynamic, args: js.Dynamic) => {
      val cols = args.sortCols.asInstanceOf[js.Array[js.Dynamic]]

      dataView.sort {
        (a: ComputedItem, b: ComputedItem) =>
          var i = 0
          var ret = 0.0
          while (i < cols.length && ret == 0) {
            val col = cols(i)
            val sign = if (cols(i).sortAsc.asInstanceOf[js.Boolean]) 1 else -1
            val a1: js.Dynamic = col.sortCol.getter(a.asInstanceOf[js.Any])
            val b1: js.Dynamic = col.sortCol.getter(b.asInstanceOf[js.Any])

            val res = a1 - b1
            if (js.isNaN(res)) {
              if (a1.toString > b1.toString) {
                ret = sign
              } else if (b1.toString > a1.toString) {
                ret = -sign
              }

            } else {
              ret = sign * res
            }

            i += 1
          }
          ret: js.Number
      }

      grid.invalidate()
      grid.render()
    })
  }

  private def addMouseover() {
    grid.onMouseEnter.subscribe((e: js.Dynamic, args: js.Any) => {
      val row = grid.getCellFromEvent(e).row
      if (row.nullSafe.isDefined) {
        val (top, bottom) = if (e.clientY / global.window.innerHeight < .5) {
          Some(e.clientY.asJsNum + 10) -> None
        } else {
          None -> Some(global.window.innerHeight - e.clientY + 10)
        }

        val (right, left) = if (e.clientX / global.window.innerWidth < .5) {
          None -> Some(e.clientX.asJsNum + 10)
        } else {
          Some(global.window.innerWidth - e.clientX + 10) -> None
        }

        val item = grid.getDataItem(row).asInstanceOf[ComputedItem]
        showItemDetail(top, right, bottom, left, item)
      }
    })
    grid.onMouseLeave.subscribe((e: js.Dynamic, args: js.Any) => {
      jq("#itemdetail").hide()
    })
  }


  def showItemDetail(
    top: Option[js.Number],
    right: Option[js.Number],
    bottom: Option[js.Number],
    left: Option[js.Number],
    item: ComputedItem) {

    val d = jq("#itemdetail")
    d.show()
    d.css("top", top.getOrElse("".toJs))
    d.css("right", right.getOrElse("".toJs))
    d.css("bottom", bottom.getOrElse("".toJs))
    d.css("left", left.getOrElse("".toJs))
    val color = item.item.getFrameType.color
    def requirements = {
      val xs = for {
        rs <- item.item.requirements.toOption.toList
        r <- rs.toList
        n <- r.name.toOption.toList
        vs <- r.values.toList
      } yield {
        s"$n ${vs(0).toString}"
      }
      xs.oIf(_.nonEmpty, _ => xs.mkString("Requires ", ", ", ""), _ => "")
    }
    def properties = {
      (for {
        props <- item.item.properties.toOption.toList
        prop <- props.toList
      } yield {
        val vs = for {
          v <- prop.values.toList
        } yield {
          v(0)
        }
        prop.name + " " + vs.mkString("")
      }).mkString("<br>")
    }
    def flavorText = {
      item.item.flavourText.toOption.map(_.toList.mkString("<br>")).getOrElse("")
    }
    val sections = List(
      item.item.name.toString,
      item.item.typeLine.toString,
      properties,
      requirements,
      item.item.descrText.toOption.map(_.toString).getOrElse(""),
      item.item.implicitModList.mkString("<br>"),
      item.item.explicitModList.mkString("<br>"),
      item.item.secDescrText.toOption.map(_.toString).getOrElse(""),
      flavorText
    ).filter(_.nonEmpty)
    val h = s"""
    <div style="color:$color;padding:5px">
    ${sections.mkString("<hr>")}
    </div>
    """

    d.html(h)

    console.log(item)
  }
}


