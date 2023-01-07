#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <time.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/resource.h>
#include <sys/wait.h>

//---- Limits for string length and number of threads
#define STRINGLEN 256
#define MAX_THREADS 100

//---- NumThreads keeps track of the amount of threads running, program can only exit when = 0
int NumThreads = 0;
//--------------------------------------------------------------------------
//---- Method used for the threads to compute a fib number and print it out
void *ComputeFibonacci(void *Input)
{
//---- Fibonacci() calculates the fib number
    long Fibonacci(long WhichNumber) {
    	//----If the 1st or second number, return the answer directly
    	if (WhichNumber <= 1)
    	{
        	return(WhichNumber);
		//----Otherwise return the sum of the previous two Fibonacci numbers
    	}
    	else
    	{
        	return(Fibonacci(WhichNumber - 2) + Fibonacci(WhichNumber - 1));
    	}
	}
	long Num = Fibonacci((long)Input);
    printf("Fib %ld is %ld\n", (long)Input, Num);
    --NumThreads;
	return(0);
}
//--------------------------------------------------------------------------
int main()
{
//---- Declaring all variables needed for program with constants for pipe name/permissions and interface name
    FILE *FIFORead;
    const char *FIFOName = "FIBOPIPE";
    int ChildPID, FibNum, i = 0, Status;
    char Input[STRINGLEN];
    pthread_t Threads[MAX_THREADS];
    struct rusage Usage;
	const int Perms = 0666;

//---- Makes pipe and checks to make sure it went thru
    if (mkfifo(FIFOName, Perms) == -1)
    {
        perror("Couldn't make pipe");
        exit(EXIT_FAILURE);
    }

//---- Forks to make another process, and checks to make sure it worked
    if ((ChildPID = fork()) == -1)
    {
        perror("Could not fork");
        exit(EXIT_FAILURE);
    }

//---- Child program executes this code which tells it to run FibInterface program
    if (ChildPID == 0)
    {
        execvp("./FibInterface", NULL);
        perror("Error in exec");
        exit(EXIT_FAILURE);
    }

//---- Parent process opens pipe for reading
    if ((FIFORead = fopen(FIFOName, "r")) == NULL)
    {
        perror("Couldnt open FIFO");
        exit(EXIT_FAILURE);
    }

//---- Reads input from the pipe and makes a thread to compute fib number from the input, closes loop if input = 0
    while (fgets(Input, STRINGLEN, FIFORead) != NULL && Input[0] != '0' && i < 100)
    {
        FibNum = atoi(Input);
		if (FibNum == 0) 
		{
			break;
		}
        if (pthread_create(&Threads[i], NULL, ComputeFibonacci, (void *)FibNum) != 0)
        {
            perror("Creating thread");
            exit(EXIT_FAILURE);
        }
        ++NumThreads;
        if (pthread_detach(Threads[i]) != 0)
        {
            perror("Detaching thread");
            exit(EXIT_FAILURE);
        }
    }

//---- Closes pipe and deletes pipe
    fclose(FIFORead);
    if (unlink(FIFOName) != 0)
    {
        perror("Deleting FIFO");
        exit(EXIT_FAILURE);
    }

//---- Waits for all threads to finish then gets the CPU usage and cleans up zombie child process
    while(NumThreads != 0)
    {
        sleep(1);
    }
    getrusage(RUSAGE_SELF, &Usage);
    printf("The process used %lds %ldms\n",
           Usage.ru_utime.tv_sec + Usage.ru_stime.tv_sec,
           Usage.ru_utime.tv_usec + Usage.ru_stime.tv_usec);
	ChildPID = waitpid(ChildPID,&Status,0);

//---- For testing
//	if (WIFEXITED(Status))
//	{
//		printf("Child %d completed with status %d\n", ChildPID,WEXITSTATUS(Status));
//	}
    return(EXIT_SUCCESS);
}
//--------------------------------------------------------------------------
