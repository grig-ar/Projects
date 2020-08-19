from main.db.ResultsDatabase import ResultsDatabase
from main.model.Result import Result


class DataRepository:

    def __init__(self):
        self.database = ResultsDatabase.get_instance()
        self.session_maker = self.database.get_session_maker()

    def insert_result(self, result: Result):
        session = self.session_maker()
        session.add(result)
        session.commit()
        session.close()

    def insert_results(self, results):
        session = self.session_maker()
        session.add_all(results)
        session.commit()
        session.close()

    def get_all(self):
        session = self.session_maker()

        results = list()
        for res in session.query(Result).all():
            results.append(res)

        session.close()
        return results

    def get_by_test_name(self, test_name: str):
        session = self.session_maker()

        results = list()
        for res in session.query(Result).filter_by(test_name=test_name).all():
            results.append(res)

        session.close()
        return results

    def get_by_backend_name(self, backend_name: str):
        session = self.session_maker()

        results = list()
        for res in session.query(Result).filter_by(backend_name=backend_name).all():
            results.append(res)

        session.close()
        return results
