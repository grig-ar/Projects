import csv


class FileWriter(object):

    def write_result(self, filename, tests):
        def write():
            csv_columns = ['Test_name', 'Result', 'Backend_name', 'Job_status', 'Date', 'Shots_taken', 'Time_taken',
                           'Accuracy', 'Counts']
            try:
                with open('\\results\\new\\%s.csv' % filename, 'w+', newline='') as file:
                    writer = csv.DictWriter(file, fieldnames=csv_columns, delimiter=";")
                    writer.writeheader()
                    for test in tests:
                        dict_data = dict.fromkeys(csv_columns)
                        dict_data['Test_name'] = test.name
                        dict_data['Result'] = test.result
                        for res in test.results:
                            dict_data['Backend_name'] = str(res['Backend_name'])
                            dict_data['Job_status'] = str(res['Job_status'])
                            dict_data['Date'] = str(res['Date'])
                            dict_data['Shots_taken'] = str(res['Shots_taken'])
                            dict_data['Time_taken'] = str(res['Time_taken'])
                            dict_data['Accuracy'] = str(res['Accuracy'])
                            # dict_data['Time_taken'] = str(round(float(dict_results['results'][0]['time_taken']), 3))
                            dict_data['Counts'] = str(res['Counts'])
                            writer.writerow(dict_data)
            except IOError:
                print('IO error')

        return write
