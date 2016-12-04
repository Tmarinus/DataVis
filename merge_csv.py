import pandas as pd
import os
import numpy as np


cases = pd.read_csv('data/test_2.01_WDB_Case_START.csv',sep = ",", index_col= 0)

# print(cases)

level_2 = []

for csv in os.listdir('data/level_2'):
    print(csv)
    level_2.append(pd.read_csv('data/level_2/'+csv,sep = ","))

print(cases.loc['C/EGD/385'])

# print(cases.shape)
for data in level_2:
    new_cols_labels = np.setdiff1d(data.columns.values,cases.columns.values)
    new_cols_labels = np.delete(new_cols_labels, np.where(new_cols_labels == 'CaseRef'))
    cases = pd.concat([cases, pd.DataFrame(columns=new_cols_labels)])
    # print(data)
    count = 0
    for row in data.T:
        row_data = data.iloc[row]
        for new_col in new_cols_labels:
            # cases.loc[row_data['CaseRef']]
            # print(cases.loc[row_data['CaseRef']])
            if not isinstance(row_data[new_col], float) or not np.isnan(row_data[new_col]):
                # print(row_data[new_col])
                # print(cases.loc[row_data['CaseRef']][new_col])
                if isinstance(cases.loc[row_data['CaseRef']][new_col], float) \
                        or np.isnan(cases.loc[row_data['CaseRef']][new_col]):
                    cases.set_value()
                    cases.loc[row_data['CaseRef']][new_col] = row_data[new_col]

    print(count)

print(cases.loc['C/EGD/385'])
# print(cases['CaseRef'])
# print(cases.columns)
# print(cases.iloc[39,:])
