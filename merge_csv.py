import pandas as pd
import os
import numpy as np

# Read initial reference file
cases = pd.read_csv('data/test_2.01_WDB_Case_START.csv',sep = ",", index_col= 0)

level_2 = []
# Read all files
for csv in os.listdir('data/level_2'):
    print(csv)
    level_2.append(pd.read_csv('data/level_2/'+csv, sep=","))

# For each file
print(cases.shape)
count = 0
for data in level_2:
    # Get newly to add column names and add them
    new_cols_labels = np.setdiff1d(data.columns.values,cases.columns.values)
    new_cols_labels = np.delete(new_cols_labels, np.where(new_cols_labels == 'CaseRef'))
    cases = pd.concat([cases, pd.DataFrame(columns=new_cols_labels)])
    print(new_cols_labels.shape)
    count += new_cols_labels.size
    # For each row in our data
    converted_columns = []
    for row in data.T:
        row_data = data.iloc[row]
        # for each column of our data
        for new_col in new_cols_labels:
            # Check if our new element is NaN
            if not isinstance(row_data[new_col], float) or not np.isnan(row_data[new_col]):
                # Check if element in existing dataset is NaN
                # We put new element in if current is Nan
                # We count up if element is already int
                # We convert element to int if it currently is not
                if isinstance(cases.loc[row_data['CaseRef']][new_col], float) \
                        and np.isnan(cases.loc[row_data['CaseRef']][new_col]):
                    cases.set_value(row_data['CaseRef'], [new_col], row_data[new_col])
                elif isinstance(cases.loc[row_data['CaseRef']][new_col], int):
                    cases.set_value(row_data['CaseRef'], [new_col], cases.loc[row_data['CaseRef']][new_col] + 1)
                else:
                    cases.set_value(row_data['CaseRef'], [new_col], 2)
                    if new_col not in converted_columns:
                        converted_columns.append(new_col)

    for row in data.T:
        row_data = data.iloc[row]
        for column in converted_columns:
            if not isinstance(row_data[column], int):
                cases.set_value(row_data['CaseRef'], [column], 1)
    # print(converted_columns)
print(count)
print(cases.shape)
cases.to_csv('data/level_2_merged.csv')
# print(cases.loc['C/EGD/385'])
# print(cases.columns)
# print(cases.iloc[39,:])
