import os

from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker

from main.model.Result import Base


class ResultsDatabase:
    __instance = None
    __session_maker = None

    @classmethod
    def get_instance(cls):
        if not cls.__instance:
            cls.__instance = ResultsDatabase()
            path = os.getcwd() + '\\schemas\\results_database.db'
            engine = create_engine('sqlite:///' + path)
            Base.metadata.create_all(engine)
            cls.__session_maker = sessionmaker(engine)
        return cls.__instance

    def get_session_maker(self) -> sessionmaker:
        return self.__session_maker
