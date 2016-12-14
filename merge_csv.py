import pandas as pd
import os
import numpy as np

# # Read initial reference file
# cases = pd.read_csv('data/test_2.01_WDB_Case_START.csv',sep = ",", index_col= 0)
#
# converted_columns = []
# level_2 = []
# # Read all files
# for csv in os.listdir('data/level_2'):
#     print(csv)
#     level_2.append(pd.read_csv('data/level_2/'+csv, sep=","))
#
# # For each file
# print(cases.shape)
# count = 0
# for data in level_2:
#     # Get newly to add column names and add them
#     new_cols_labels = np.setdiff1d(data.columns.values,cases.columns.values)
#     new_cols_labels = np.delete(new_cols_labels, np.where(new_cols_labels == 'CaseRef'))
#     cases = pd.concat([cases, pd.DataFrame(columns=new_cols_labels)])
#     print(new_cols_labels.shape)
#     count += new_cols_labels.size
#     # For each row in our data
#     for row in data.T:
#         row_data = data.iloc[row]
#         # for each column of our data
#         for new_col in new_cols_labels:
#             # Check if our new element is NaN
#             if not isinstance(row_data[new_col], float) or not np.isnan(row_data[new_col]):
#                 # Check if element in existing dataset is NaN
#                 # We put new element in if current is Nan
#                 # We count up if element is already int
#                 # We convert element to int if it currently is not
#                 if isinstance(cases.loc[row_data['CaseRef']][new_col], float) \
#                         and np.isnan(cases.loc[row_data['CaseRef']][new_col]):
#                     cases.set_value(row_data['CaseRef'], [new_col], row_data[new_col])
#                 elif isinstance(cases.loc[row_data['CaseRef']][new_col], int) and "Ref" not in new_col and "ounty" not in new_col:
#                     cases.set_value(row_data['CaseRef'], [new_col], cases.loc[row_data['CaseRef']][new_col] + 1)
#                 elif "Ref" not in new_col and "ounty" not in new_col:
#                     cases.set_value(row_data['CaseRef'], [new_col], 2)
#                     if new_col not in converted_columns:
#                         converted_columns.append(new_col)
# #
# level_1 = []
# # Read all files
# for csv in os.listdir('data/level_1'):
#     print(csv)
#     level_1.append(pd.read_csv('data/level_1/' + csv, sep=","))
#
# # For each file
# # print(cases.shape)
# count = 0
# for data in level_1:
#     # Get newly to add column names and add them
#     new_cols_labels = np.setdiff1d(data.columns.values, cases.columns.values)
#     new_cols_labels = np.delete(new_cols_labels, np.where(new_cols_labels == 'AccusedRef'))
#     cases = pd.concat([cases, pd.DataFrame(columns=new_cols_labels)])
#     print(new_cols_labels.shape)
#     count += new_cols_labels.size
#     # For each row in our data
#     for row in data.T:
#         row_data = data.iloc[row]
#         # for each column of our data
#         for new_col in new_cols_labels:
#             # Check if our new element is NaN
#             if not isinstance(row_data[new_col], float) or not np.isnan(row_data[new_col]):
#                 # Check if element in existing dataset is NaN
#                 # We put new element in if current is Nan
#                 # We count up if element is already int
#                 # We convert element to int if it currently is not
#                 # cases.loc[cases['AccusedRef'] == row_data['AccusedRef']]
#                 # print(cases.loc[cases['AccusedRef'] == row_data['AccusedRef']].iloc[0][new_col])
#                 # print(row_data)
#                 if isinstance(cases.loc[cases['AccusedRef'] == row_data['AccusedRef']].iloc[0][new_col], float) \
#                         and np.isnan(cases.loc[cases['AccusedRef'] == row_data['AccusedRef']].iloc[0][new_col]):
#                     cases.set_value(cases.loc[cases['AccusedRef'] == row_data['AccusedRef']].index, [new_col], row_data[new_col])
#                 elif isinstance(cases.loc[cases['AccusedRef'] == row_data['AccusedRef']].iloc[0][new_col],
#                                 int) and "Ref" not in new_col and "ounty" not in new_col:
#                     cases.set_value(cases.loc[cases['AccusedRef'] == row_data['AccusedRef']].index, [new_col],
#                                     cases.loc[cases['AccusedRef'] == row_data['AccusedRef']].iloc[0][new_col] + 1)
#                 elif "Ref" not in new_col and "ounty" not in new_col:
#                     # print(cases.loc[cases['AccusedRef'] == row_data['AccusedRef']].index)
#                     cases.set_value(cases.loc[cases['AccusedRef'] == row_data['AccusedRef']].index, [new_col], 2)
#                     if new_col not in converted_columns:
#                         converted_columns.append(new_col)
#
# level_3_solo = pd.read_csv('data/Copy of 3.11_WDB_Trial.csv',sep = ",", index_col= 0)
# # print(level_3_solo)
# # print(level_3_solo['CaseRef'])
# cases = pd.concat([cases, pd.DataFrame(columns=['Trialref'])])
# for row in level_3_solo.T:
#     # print(row)
#     # print(level_3_solo.loc[row])
#     # print(level_3_solo.loc[row]['CaseRef'])
#     # print(cases.loc[level_3_solo.loc[row]['CaseRef']])
#     cases.set_value(level_3_solo.loc[row]['CaseRef'], ['Trialref'], row)
#
# print(level_3_solo.shape)
# print(cases['Trialref'].shape)
#
# level_3 = []
# # Read all files
# for csv in os.listdir('data/level_3'):
#     print(csv)
#     level_3.append(pd.read_csv('data/level_3/' + csv, sep=","))
#
# print(cases.loc[cases['Trialref'] == 'T/JO/606'])
# exit()
# # For each file
# # print(cases.shape)
# count = 0
# for data in level_3:
#     # Get newly to add column names and add them
#     new_cols_labels = np.setdiff1d(data.columns.values, cases.columns.values)
#     new_cols_labels = np.delete(new_cols_labels, np.where(new_cols_labels == 'Trialref'))
#     cases = pd.concat([cases, pd.DataFrame(columns=new_cols_labels)])
#     print(new_cols_labels.shape)
#     count += new_cols_labels.size
#     # For each row in our data
#     for row in data.T:
#         row_data = data.iloc[row]
#         # for each column of our data
#         for new_col in new_cols_labels:
#             # Check if our new element is NaN
#             if not isinstance(row_data[new_col], float) or not np.isnan(row_data[new_col]):
#                 if cases.loc[cases['Trialref'] == row_data['Trialref']].size == 0:
#                     print(cases.loc[cases['Trialref'] == row_data['Trialref']].size)
#                     print(row_data['Trialref'])
#                     continue
#                 # Check if element in existing dataset is NaN
#                 # We put new element in if current is Nan
#                 # We count up if element is already int
#                 # We convert element to int if it currently is not
#                 # cases.loc[cases['AccusedRef'] == row_data['AccusedRef']]
#                 # print(cases.loc[cases['AccusedRef'] == row_data['AccusedRef']].iloc[0][new_col])
#                 # print(row_data)
#                 # print(cases.loc[cases['Trialref'] == row_data['Trialref']].iloc[0][new_col].__class__)
#                 # print(cases.loc[cases['Trialref'] == row_data['Trialref']].size)
#                 # print("\n")
#                 if isinstance(cases.loc[cases['Trialref'] == row_data['Trialref']].iloc[0][new_col], float) \
#                         and np.isnan(cases.loc[cases['Trialref'] == row_data['Trialref']].iloc[0][new_col]):
#                     cases.set_value(cases.loc[cases['Trialref'] == row_data['Trialref']].index, [new_col], row_data[new_col])
#                 elif isinstance(cases.loc[cases['Trialref'] == row_data['Trialref']].iloc[0][new_col],
#                                 int) and "Ref" not in new_col and "ounty" not in new_col:
#                     cases.set_value(cases.loc[cases['Trialref'] == row_data['Trialref']].index, [new_col],
#                                     cases.loc[cases['Trialref'] == row_data['Trialref']].iloc[0][new_col] + 1)
#                 elif "Ref" not in new_col and "ounty" not in new_col:
#                     # print(cases.loc[cases['AccusedRef'] == row_data['AccusedRef']].index)
#                     cases.set_value(cases.loc[cases['Trialref'] == row_data['Trialref']].index, [new_col], 2)
#                     if new_col not in converted_columns:
#                         converted_columns.append(new_col)
#

# Read initial reference file
cases = pd.read_csv('data/Copy of 1.01_WDB_Accused.csv',sep = ",", index_col= 0)

converted_columns = []
level_2 = []
# Read all files
for csv in os.listdir('data/level_1'):
    print(csv)
    level_2.append(pd.read_csv('data/level_1/'+csv, sep=","))

# For each file
print(cases.shape)
count = 0
for data in level_2:
    # Get newly to add column names and add them
    new_cols_labels = np.setdiff1d(data.columns.values,cases.columns.values)
    new_cols_labels = np.delete(new_cols_labels, np.where(new_cols_labels == 'AccusedRef'))
    cases = pd.concat([cases, pd.DataFrame(columns=new_cols_labels)])
    print(new_cols_labels.shape)
    count += new_cols_labels.size
    # For each row in our data
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
                if isinstance(cases.loc[row_data['AccusedRef']][new_col], float) \
                        and np.isnan(cases.loc[row_data['AccusedRef']][new_col]):
                    cases.set_value(row_data['AccusedRef'], [new_col], row_data[new_col])
                elif isinstance(cases.loc[row_data['AccusedRef']][new_col], int) and "Ref" not in new_col and "ounty" not in new_col:
                    cases.set_value(row_data['AccusedRef'], [new_col], cases.loc[row_data['AccusedRef']][new_col] + 1)
                elif "Ref" not in new_col and "ounty" not in new_col:
                    cases.set_value(row_data['AccusedRef'], [new_col], 2)
                    if new_col not in converted_columns:
                        converted_columns.append(new_col)
#

# exit()
# print(cases)
for column in converted_columns:
    # print(cases[column])
    for row in cases.index:
        # print(cases[column][row])
        if not pd.isnull(cases[column][row]) and isinstance(cases[column][row], str):
            # print(row)
            cases.set_value(row, column, 1)

    # print(row_data[column].__class__)
    # if not isinstance(row_data[column], float):
    #     cases.set_value(row_data['CaseRef'], [column], 1)
# for row in cases.T:
#     print(row)
# print(cases.columns)
print(converted_columns)
print(count)
print(cases.shape)
cases.to_csv('data/level_1_merged.csv')
# print(cases.loc['C/EGD/385'])
# print(cases.columns)
# print(cases.iloc[39,:])
