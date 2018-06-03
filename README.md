# Sort Big File in Java
Ý tưởng:
  - Đọc file theo từng block
  - Với mỗi block được đọc lên, sort và lưu vào file tạm
  - Lấy phần tử đầu tiên của mỗi file tạm
  - Sort và lưu phần tử đầu tiên vào file output
  - Lấy phần tử tiếp theo trong file chứa phần tử vừa lưu vào file
  - Lặp lại cho tới khi không còn phần tử nào trong các file tạm
# Run
```
java -jar target/sortbigfile-1.0-SNAPSHOT.jar input.txt output.txt
Trong đó: input.txt: input file
          output.txt: output file
```
# Sample code
```
import com.huylvq.sortbigfile;

//... inputFile: input file name
//... outputFile: output file name

// Use single thread sort
SortBigFile sorter = new SortBigFile(inputFile, outputFile);
sorter.sort();

// Use multithread sort
SortBigFileUsingMultithread sorter = new SortBigFileUsingMultithread(inputFile, outputFile);
sorter.sort();
```
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

# Run
```
java -jar target/sortbigfile-1.0-SNAPSHOT.jar input.txt output.txt

with input.txt: input file
     output.txt: output file
```
  
