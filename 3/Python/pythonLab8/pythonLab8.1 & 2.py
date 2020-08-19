import random

class Warrior:
    def setName(self, k):
        self.name = k
    def setPhysicalResistance(self, n):
        self.physicalResistance = n
    def setHealthPoints(self, m):
        self.healthPoints = m
    def setAttackType(self, at):
        self.attackType = at
    def setAttackPoints(self, a):
        self.attackPoints = a
    def setBackfireDamage(self, b):
        self.backfireDamage = b
    def getName(self):
        try:
            return self.name
        except:
            print("No name")
    def getAttackType(self):
        try:
            return self.attackType
        except:
            print("Can't attack")
    def getAttackPoints(self):
        try:
            return self.attackPoints
        except:
            return 0
    def getPhysicalResistance(self):
        try:
            return self.physicalResistance
        except:
            return 0
    def getHealthPoints(self):
        try:
            return self.healthPoints
        except:
            return 1
    def getBackfireDamage(self):
        try:
            return self.backfireDamage
        except:
            return 0
    def meleeAttack(self, enemy):
        enemy.healthPoints -= self.getAttackPoints() * (1 - enemy.getPhysicalResistance())
        print(self.getName() + ' attacked ' + enemy.getName() + ' for ' + str(self.getAttackPoints() * (1 - enemy.getPhysicalResistance())) + ' HP')
        if (enemy.getHealthPoints() > 0):
            self.healthPoints -= enemy.getBackfireDamage() * (1 - self.getPhysicalResistance())
            print(enemy.getName() + ' striked back for ' + str(enemy.getBackfireDamage() * (1 - self.getPhysicalResistance())) + ' HP')

class Archer:
    def setName(self, k):
        self.name = k
    def setPhysicalResistance(self, n):
        self.physicalResistance = n
    def setHealthPoints(self, m):
        self.healthPoints = m
    def setAttackType(self, at):
        self.attackType = at
    def setAttackPoints(self, a):
        self.attackPoints = a
    def setBackfireDamage(self, b):
        self.backfireDamage = b
    def getName(self):
        try:
            return self.name
        except:
            print("No name")
    def getAttackType(self):
        try:
            return self.attackType
        except:
            print("Can't attack")
    def getAttackPoints(self):
        try:
            return self.attackPoints
        except:
            return 0
    def getPhysicalResistance(self):
        try:
            return self.physicalResistance
        except:
            return 0
    def getHealthPoints(self):
        try:
            return self.healthPoints
        except:
            return 1
    def getBackfireDamage(self):
        try:
            return self.backfireDamage
        except:
            return 0
    def rangedAttack(self, enemy):
        enemy.healthPoints -= self.getAttackPoints() * (1 - enemy.getPhysicalResistance())
        print(self.getName() + ' attacked ' + enemy.getName() + ' for ' + str(self.getAttackPoints() * (1 - enemy.getPhysicalResistance())) + ' HP')

class ConstructWarrior:
    def __init__(self, name, physRes, hp, ap, bfd):
        self.name = name
        self.physicalResistance = physRes
        self.healthPoints = hp
        self.attackType = 'melee'
        self.attackPoints = ap
        self.backfireDamage = bfd
    def getName(self):
        try:
            return self.name
        except:
            print("AnonW")
    def getAttackType(self):
        try:
            return self.attackType
        except:
            print("Can't attack")
    def getAttackPoints(self):
        try:
            return self.attackPoints
        except:
            return 0
    def getPhysicalResistance(self):
        try:
            return self.physicalResistance
        except:
            return 0
    def getHealthPoints(self):
        try:
            return self.healthPoints
        except:
            return 1
    def getBackfireDamage(self):
        try:
            return self.backfireDamage
        except:
            return 0
    def meleeAttack(self, enemy):
        enemy.healthPoints -= self.getAttackPoints() * (1 - enemy.getPhysicalResistance())
        print(self.getName() + ' attacked ' + enemy.getName() + ' for ' + str(self.getAttackPoints() * (1 - enemy.getPhysicalResistance())) + ' HP')
        if (enemy.getHealthPoints() > 0):
            self.healthPoints -= enemy.getBackfireDamage() * (1 - self.getPhysicalResistance())
            print(enemy.getName() + ' striked back for ' + str(enemy.getBackfireDamage() * (1 - self.getPhysicalResistance())) + ' HP')

warrior1 = Warrior()
warrior1.setName('W')
warrior1.setPhysicalResistance(0.3)
warrior1.setHealthPoints(120)
warrior1.setAttackType('melee')
warrior1.setAttackPoints(25)
warrior1.setBackfireDamage(10)

warrior2 = ConstructWarrior('W2', 0.3, 150, 20, 10)

archer1 = Archer()
archer1.setName('A')
archer1.setPhysicalResistance(0.1)
archer1.setHealthPoints(100)
archer1.setAttackType('ranged')
archer1.setAttackPoints(35)
archer1.setBackfireDamage(5)

###1###

endGame = False
print('Let the battle begin!')
while(not endGame):
    print(warrior1.getName() + ':' + str(warrior1.getHealthPoints()) + ' HP')
    print(archer1.getName() + ':' + str(archer1.getHealthPoints()) + ' HP')
    coin = random.randrange(0,2,1)
    if (coin == 0):
        print('Warrior turn')
        warrior1.meleeAttack(archer1)
    if (coin == 1):
        print('Archer turn')
        archer1.rangedAttack(warrior1)
    if (warrior1.getHealthPoints() <= 0):
        print(warrior1.getName() + ' dies.')
        print(archer1.getName() + ' is victorious!')
        endGame = True
    if (archer1.getHealthPoints() <= 0):
        print(archer1.getName() + ' dies.')
        print(warrior1.getName() + ' is victorious!')
        endGame = True

###2###

#endGame = False
#print('Let the battle begin!')
#while(not endGame):
#    print(warrior1.getName() + ' : ' + str(warrior1.getHealthPoints()) + ' HP')
#    print(warrior2.getName() + ' : ' + str(warrior2.getHealthPoints()) + ' HP')
#    coin = random.randrange(0,2,1)
#    if (coin == 0):
#        print('Warrior1 turn')
#       warrior1.meleeAttack(warrior2)
#    if (coin == 1):
#        print('Warrior2 turn')
#        warrior2.meleeAttack(warrior1)
#    if (warrior1.getHealthPoints() <= 0):
#        print(warrior1.getName() + ' dies.')
#        print(warrior2.getName() + ' is victorious!')
#        endGame = True
#    if (warrior2.getHealthPoints() <= 0):
#        print(warrior2.getName() + ' dies.')
#        print(warrior1.getName() + ' is victorious!')
#        endGame = True
#    print()




    
    
