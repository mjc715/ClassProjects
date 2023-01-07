#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <string.h>

//---- Limit to string
#define MAX_STRING 256

int main()
{
//---- Declares variables needed for program
    FILE *FIFOWrite;
    char Input[MAX_STRING];
	const char* FIFOName = "FIBOPIPE";

//---- Opens pipe for writing
    if ((FIFOWrite = fopen(FIFOName, "w")) == NULL)
    {
        perror("Couldnt open FIFO to write");
        exit(EXIT_FAILURE);
    }

//---- Gets input from keyboard and writes and flushes it thru the pipe
    printf("Enter fib number to calcuate: \n");
    while (fgets(Input, MAX_STRING, stdin) != NULL && Input[0] != '0')
    {
        fprintf(FIFOWrite, Input);
        fflush(FIFOWrite);
        printf("Enter fib number to calcuate: \n");
    }

//---- Closes the pipe and exits
    fclose(FIFOWrite);
    return(EXIT_SUCCESS);
}
