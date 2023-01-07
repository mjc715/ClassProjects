Text editor commands have a line range specification and edit operation.  

Line range specs:  
/(text)/ : edit all lines that contain (text)  
(Number1),(Number2)/ : edit all lines in range Number1 to Number2, inclusive  
No specification : edit all lines  

Edit operations:  
A(text) : appends (text) at the end of a line  
I(text) : inserts (text) at the start of a line  
O(text) : inserts (text) on a new line before the input line  
s/(text1)/(text2)/ : replaces the first occurance of (text1) with (text2)  
d : deletes line  
