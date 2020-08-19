from sqlalchemy import Column, Integer, String
from sqlalchemy.ext.declarative import declarative_base

Base = declarative_base()


class Result(Base):
    __tablename__ = 'results_table'

    id = Column(Integer, primary_key=True, nullable=False)
    test_name = Column(String)
    result = Column(Integer)
    backend_name = Column(String)
    job_status = Column(String)
    date = Column(String)
    shots = Column(Integer)
    time = Column(String)
    counts = Column(String)
    accuracy = Column(String)

    def __repr__(self):
        return "<Result(test_name='%s', result='%s', backend_name='%s', job_status='%s', date='%s', shots='%s'" \
               ", time='%s', counts='%s', accuracy='%s')>" % (
                   self.test_name, str(self.result), self.backend_name, self.job_status, self.date, str(self.shots),
                   self.time, self.counts, self.accuracy)

        # fields = ['Test_name', 'Result', 'Backend_name', 'Job_status', 'Date', 'Shots_taken', 'Time_taken',
        #           'Accuracy', 'Counts']

    @staticmethod
    def create_result_from_dict(dict_result):
        return Result(test_name=dict_result['Test_name'], result=dict_result['Result'],
                      backend_name=dict_result['Backend_name'], job_status=dict_result['Job_status'],
                      date=dict_result['Date'], shots=int(dict_result['Shots_taken']), time=dict_result['Time_taken'],
                      counts=str(dict_result['Counts']), accuracy=dict_result['Accuracy'])
