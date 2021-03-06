package looty
package mods

import looty.model.{Attributes, LifeAndMana, Elements}


//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/26/13 5:04 PM
//////////////////////////////////////////////////////////////

trait ProperItemProp0 {
  def apply(i: ProperItem)
}
object ProperItemProp1 {
  def apply(): ProperItemProp1 = ???
}
trait ProperItemProp1 {
  def apply(i: ProperItem, a: Double)
}

object ProperItemProp2 {
  def apply(): ProperItemProp2 = ???
}
trait ProperItemProp2 {
  def apply(i: ProperItem, a: Double, b: Double)
}

object ProperItem {

}


class ProperItem {

  private def p1() = ProperItemProp1()
  private def p2() = ProperItemProp2()


  object damage {
    val local = Elements of p2()
    val base  = Elements of p2()
  }

  object increased {
    val movementSpeed = p1()


    val manaRegenerationRate = p1()

    val quantityOfItemsFound = p1()
    val rarityOfItemsFound   = p1()

    val flaskChargesGained = p1()

    val flaskEffectDuration = p1()

    //    val localPhysicalDamage = Elements.of(p1())
    val localPhysicalDamage = p1()
    val elementalDamage     = Elements of p1()

    val localArmour  = p1()
    val globalArmour = p1()

    val localEnergyShield  = p1()
    val globalEnergyShield = p1()

    val localEvasion  = p1()
    val globalEvasion = p1()

    val flaskRecoveryRate = LifeAndMana of p1()

    val spellDamage                = p1()
    val elementalDamageWithWeapons = p1()

    val projectileSpeed = p1()
    val accuracyRating  = p1()
    val attackSpeed     = p1()
    val castSpeed       = p1()

    val globalCriticalStrikeMultiplier = p1()
    val globalCriticalStrikeChance     = p1()
    val localCriticalStrikeChance      = p1()
    val spellCriticalStrikeChance      = p1()

    val blockAndStunRecovery  = p1()
    val stunDurationOnEnemies = p1()

  }

  object reduced {
    val flaskChargesUsed      = p1()
    val attributeRequirements = p1()
    val enemyStunThreshold    = p1()
  }

  object other {
    val reflects                 = p1()
    val additionalBlockChance    = p1()
    val lifeOnHit                = p1()
    val lifeRegeneratedPerSecond = p1()
    val lifeAndManaOnKill        = LifeAndMana of p1()
    val lifeAndManaLeech         = LifeAndMana of p1()
  }

  object plusTo {
    val localArmour  = p1()
    val globalArmour = p1()

    val localEnergyShield  = p1()
    val globalEnergyShield = p1()

    val localEvasion  = p1()
    val globalEvasion = p1()

    val allAttributes = p1()

    val attributes    = Attributes of p1()
    val resistance    = Elements of p1()
    val allResistance = p1()

    val lifeAndMana = LifeAndMana of p1()
  }

  object gem {
    val bow       = p1()
    val minion    = p1()
    val melee     = p1()
    val cold      = p1()
    val fire      = p1()
    val lightning = p1()
    val any       = p1()
    def apply(g: String) = {
      g match {
        case "Bow" => Some(bow)
        case "Minion" => Some(minion)
        case "Melee" => Some(melee)
        case "Cold" => Some(cold)
        case "Fire" => Some(fire)
        case "Lightning" => Some(lightning)
        case "" => Some(any)
        case _ => None
      }
    }
  }

  //  object increased {
  //    val damage                         = Elements mutable 0.0
  //    var stunDurationOnEnemies          = 0.0
  //    var chillDurationOnEnemies         = 0.0
  //    var attackSpeed                    = 0.0
  //    var globalCriticalStrikeMultiplier = 0.0
  //    var globalCriticalStrikeChance     = 0.0
  //    var criticalStrikeChance           = 0.0
  //    var criticalStrikeChanceForSpells  = 0.0
  //    var armour                         = 0.0
  //    var evasion                        = 0.0
  //    var energyShield                   = 0.0
  //    var maximumEnergyShield            = 0.0
  //    var quantityOfItemsFound           = 0.0
  //    var rarityOfItemsFound             = 0.0
  //    var movementSpeed                  = 0.0
  //    var blockAndStunRecovery           = 0.0
  //    var spellDamage                    = 0.0
  //    var manaRegenerationRate           = 0.0
  //    var elementalDamageWithWeapons     = 0.0
  //    var lightRadius                    = 0.0
  //    var castSpeed                      = 0.0
  //    var projectileSpeed                = 0.0
  //    var accuracyRating                 = 0.0
  //    var blockRecovery                  = 0.0
  //    var elementalDamage                = 0.0
  //  }


  //
  //  lazy val maxLinks: Int       = item.sockets.toOption.map(_.toList.map(_.group).groupBy(x => x).map(_._2.size).maxOpt.getOrElse(0)).getOrElse(0)
  //  lazy val score   : ItemScore = ItemScorer(this).getOrElse(ItemScore(Nil, 0))
  //
  //  def maxResist = plusTo.resistance.all.max
  //  def magicFind = increased.quantityOfItemsFound + increased.rarityOfItemsFound
  //
  //  def isEquippable = !item.isGem && !item.isCurrency && !item.isMap && !item.isQuest
  //
  //  def displayName = {
  //    var n = item.name
  //    if (n.nullSafe.isEmpty || n.isEmpty) n = item.typeLine
  //    n
  //  }
  //
  //  var location = ""
  //  def typeName = {
  //    if (slots.isAmulet) "Amulet"
  //    else if (slots.isRing) "Ring"
  //    else if (slots.isHelmet) "Helmet"
  //    else if (slots.isChest) "Chest"
  //    else if (slots.isGloves) "Gloves"
  //    else if (slots.isBoots) "Boots"
  //    else if (slots.isBelt) "Belt"
  //    else if (slots.isShield) "Shield"
  //    else if (slots.isQuiver) "Quiver"
  //    else if (slots.isFlask) "Flask"
  //    else if (slots.isWeapon) properties.weaponType.toShortString
  //    else if (item.isCurrency) "Currency"
  //    else if (item.isGem) "Gem"
  //    else if (item.isMap) "Map"
  //    else if (item.isQuest) "QuestItem"
  //    else "UNKNOWN ITEM TYPE"
  //  }
  //
  //
  //  object reduced {
  //    var attributeRequirements = 0.0
  //    var enemyStunThreshold    = 0.0
  //  }
  //
  //  var sockets: List[List[String]] = Nil
  //
  //  object requirements {
  //    var level     = 0.0
  //    var attribute = Attributes.mutable(0.0)
  //  }
  //
  //  val damages = Elements of MinMaxDamage(0, 0)
  //
  //  object plusTo {
  //    val attribute  = Attributes mutable 0.0
  //    val resistance = Elements mutable 0.0
  //    def totalResistance = resistance.all.sum
  //    val lifeAndMana    = LifeAndMana mutable 0.0
  //    var accuracyRating = 0.0
  //    var evasionRating  = 0.0
  //    var armour         = 0.0
  //    var energyShield   = 0.0
  //  }
  //
  //  object leech {var physical = LifeAndMana mutable 0.0}
  //  object onKill {var lifeAndMana = LifeAndMana mutable 0.0}
  //  object onHit {var lifeAndMana = LifeAndMana mutable 0.0}
  //  object gemLevel {
  //    val element   = Elements mutable 0.0
  //    val attribute = Attributes mutable 0.0
  //    var melee     = 0.0
  //    var minion    = 0.0
  //    var bow       = 0.0
  //    var any       = 0.0
  //    def addToAll(n: Double) = {
  //      Elements.all.foreach(element +=(_, n))
  //      Attributes.all.foreach(attribute +=(_, n))
  //      melee += n
  //      minion += n
  //      bow += n
  //      any += n
  //    }
  //    def max = (List(melee, minion, bow) ::: attribute.all ::: element.all).max
  //  }
  //
  //  object total {
  //    def dps: Double = perElementDps.all.sum
  //    val perElementDps = Elements calculatedWith { element =>
  //      properties.damages(element).avg * {
  //        var x = properties.attacksPerSecond
  //        if (x == 0.0) x = 1.0
  //        x
  //      }
  //    }
  //    def critChance = (100 + increased.globalCriticalStrikeChance) / 100.0 *
  //        properties.criticalStrikeChance
  //  }
  //
  //  object slots {
  //    def is1H: Boolean = properties.weaponType.is1H
  //    def is2H: Boolean = properties.weaponType.is2H
  //    def isWeapon: Boolean = properties.weaponType.isWeapon
  //    def isFlask = item.isFlask
  //    var isSpiritShield = false
  //    var isShield       = false
  //
  //    var isHelmet = false
  //    var isChest  = false
  //    var isGloves = false
  //    var isAmulet = false
  //    var isRing   = false
  //    var isBelt   = false
  //    var isBoots  = false
  //    var isQuiver = false
  //  }
  //
  //
  //  object properties {
  //    var weaponType: WeaponType = WeaponTypes.NoWeaponType
  //    var armour                 = 0.0
  //    var energyShield           = 0.0
  //    var evasionRating          = 0.0
  //    val damages                = Elements of MinMaxDamage(0, 0)
  //    var quality                = 0.0
  //    var criticalStrikeChance   = 0.0
  //    var attacksPerSecond       = 0.0
  //    var chanceToBlock          = 0.0
  //  }
  //
  //  var reflectsPhysicalDamageToAttackers = 0.0
  //  var blockChance                       = 0.0
  //
  //  val regeneratedPerSecond = LifeAndMana mutable 0.0
  //
  //  object flask {
  //    object increased {
  //      var lifeRecoveryRate   = 0.0
  //      var effectDuration     = 0.0
  //      var manaRecoveryRate   = 0.0
  //      var flaskRecoverySpeed = 0.0
  //      var chargesGained      = 0.0
  //      var chargeRecovery     = 0.0
  //      var stunRecovery       = 0.0
  //      var recoverySpeed      = 0.0
  //      var amountRecovered    = 0.0
  //      var recoveryOnLowLife  = 0.0
  //      var lifeRecovered      = 0.0
  //      var armour             = 0.0
  //      var evasion            = 0.0
  //      var movementSpeed      = 0.0
  //    }
  //
  //    object reduced {
  //      var amountRecovered  = 0.0
  //      var recoverySpeed    = 0.0
  //      var flaskChargesUsed = 0.0
  //    }
  //
  //    var extraCharges                 = 0.0
  //    var amountAppliedInstantly       = 0.0
  //    var chargesOnCriticalStrikeTaken = 0.0
  //    var chargesOnCriticalStrikeGiven = 0.0
  //    var lifeFromMana                 = 0.0
  //
  //    var additionalResistances = 0.0
  //    var lifeRecoveryToMinions = 0.0
  //
  //    var dispelsFrozenAndChilled = false
  //    var dispelsShocked          = false
  //    var dispelsBurning          = false
  //    var removesBleeding         = false
  //    var curseImmunity           = false
  //    var knockback               = false
  //    var instantRecovery         = false
  //    var instantRecoveryLowLife  = false
  //  }

}