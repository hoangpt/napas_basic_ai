# Test Cases for Add Operation

| Test Case ID | Input 1 | Input 2 | Expected Output | Description |
|--------------|---------|---------|-----------------|-------------|
| TC_ADD_01    | 1       | 1       | 2               | Adding two positive integers. |
| TC_ADD_02    | -1      | -1      | -2              | Adding two negative integers. |
| TC_ADD_03    | 0       | 0       | 0               | Adding two zeros. |
| TC_ADD_04    | 1.5     | 2.5     | 4.0             | Adding two positive floating-point numbers. |
| TC_ADD_05    | -1.5    | -2.5    | -4.0            | Adding two negative floating-point numbers. |
| TC_ADD_06    | 1       | -1      | 0               | Adding a positive and a negative integer. |
| TC_ADD_07    | 1.5     | -2.5    | -1.0            | Adding a positive floating-point number and a negative floating-point number. |
| TC_ADD_08    | 999999  | 1       | 1000000         | Adding a large integer and a small integer. |
| TC_ADD_09    | 1e10    | 1e10    | 2e10            | Adding two large numbers in scientific notation. |
| TC_ADD_10    | 0.1     | 0.2     | 0.3             | Adding two floating-point numbers with precision issues. |