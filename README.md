# Sort Big File in Java
Idea:
  - Read input file line by line into memory
  - When the memory reach the limit, sort the list storing lines from file and write down it into temporary file
  - Continue until complete reading input file
  - Open all temporary files 
  - Read the first item of each file into 1 list
  - Sort this list then remove and write down the first item in list into output file
  - Get the next line in the temporary file containing the item writing down
  - Continue until no string left in list

Usage:
  - Provide 2 arguments for binary: input file path and output file path
  
