from abc import ABC, abstractmethod


class Department:

    def __init__(self, name):
        self.name = name

    def getName(self):
        try:
            return self.name
        except:
            return 'Anonymous department'

    def setDepartmentBoss(self, name):
        if (isinstance(name, CEO)):
            self.departmentBossName = name
        else:
            print(name + ' can not be boss of the ' + self.name)


class Employee(ABC):
    
    @abstractmethod
    def getName(self):
        pass

    @abstractmethod
    def startWorking(self, department):
        pass

    @abstractmethod
    def setBoss(self, boss):
        pass

    @abstractmethod
    def getBoss(self, boss):
        pass
    
    @abstractmethod
    def getWorkingPlace(self):
        pass

    @abstractmethod
    def hire(self, employee):
        pass

    @abstractmethod
    def fire(self, employee):
        pass

    @abstractmethod
    def getDescription(self):
        pass


class CEO(Employee):

    def __init__(self, name):
        self.name = name
        self.inferiors = []

    def setBoss(self):
        print("You can't set boss for me. I AM CEO")

    def getBoss(self):
        print('ME')

    def getName(self):
        try:
            return self.name
        except:
            return 'Anon'

    def startWorking(self, department):
        self.department = department
        department.setDepartmentBoss(self)
        print("Now I'm boss of the " + department.getName() + ' department')

    def getWorkingPlace(self):
        try:
            return self.department
        except:
            return 'Nowhere'

    def hire(self, employee):
        if not(isinstance(employee, CEO)):
            self.inferiors.append(employee)
        else:
            print(employee.getName() + ' can not be inferior of the ' + self.getName())

    def fire(self, employee):
        if(employee is in self.inferiors):
            self.inferiors.remove(employee)
        else:
            print(self.getName() + 'Can not fire ' + employee.getName())

    def getDescription(self):
        print('My name is ' + self.getName() + ' and I am working at ' + self.getWorkingPlace())
        print('My boss: ' + self.getBoss())
        print('My inferiors:')
        for infer in self.inferiors:
            infer.getDescription()


    
