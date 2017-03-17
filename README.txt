My compiler does everything correct up until type checking in which some cases might still be a bit off in some cases.

The code generation outputs into a file output.c
It is mostly working but I could not find a good solution to generate the code for some r_expr's particularly for the if statements and so those all go to if(false)