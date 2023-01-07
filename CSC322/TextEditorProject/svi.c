#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

//---- Declare variables/structures/limits needed for the program
#define MAX_COMMANDS 100
#define MAX_STRING 256

typedef char String[MAX_STRING];

int NumEdits = 0;

typedef struct
{
	enum LineSpecType
	{
		ALL_LINES = 1,
		TEXT = 2,
		LINENUM = 3
	} LineSpecType;

	union LineType
	{

		int Range[2];

		struct InsertBefore
		{
			int LineNumber;
		} InsertBefore;

		struct PhraseSearch
		{
			String KeyPhrase;
		} PhraseSearch;

	} LineType;

	char Specifier;
	String Data;

} EditCommand;
//---------------------------------------------
void FillArray(EditCommand EditCommands[], FILE *TextFile)
{
	String L1;
	EditCommand EditCommand;
	int i = 0;
	//---- Reads text file and inputs edit command specs into array of EditCommand
	while (fgets(L1, MAX_STRING, TextFile) != NULL)
	{
		int j = 0;

		//---- Checking for text search edit commands, and copies the appropriate data into the struct
		if (L1[0] == '/')
		{
			//---- Working, dont mess with this
			EditCommand.LineSpecType = TEXT;
			String Copy;
			strncpy(Copy, L1, strlen(L1));
			char *Dupe = strstr(L1 + 1, "/") + 1;
			char *delim = "/";
			char *token = strtok(Copy + 1, delim);
			strncpy(EditCommand.LineType.PhraseSearch.KeyPhrase, token + 1, strlen(token));
			strncpy(L1, Dupe, strlen(Dupe) + 1);
			EditCommand.Specifier = L1[0];
			strncpy(EditCommand.Data, L1 + 1, strlen(L1));
		}
		//----- Checking for range of line numbers for the edit command
		else if (isdigit((int)(L1[0])))
		{
			EditCommand.LineSpecType = LINENUM;
			char *delims = ",/";
			String Copy;
			strncpy(Copy, L1, strlen(L1));
			char *spec = strstr(Copy, "/") + 1;
			strncpy(L1, strstr(L1, "/") + 1, strlen(L1));
			EditCommand.Specifier = L1[0];
			strncpy(EditCommand.Data, L1 + 1, strlen(L1));
			char *token = strtok(Copy, delims);
			while (j < 2 && (token != NULL))
			{
				EditCommand.LineType.Range[j] = atoi(token);
				token = strtok(NULL, delims);
				j++;
			}
		}
		//----- If not picked up by other if statements, command applies to all lines
		else
		{
			EditCommand.LineSpecType = ALL_LINES;
			EditCommand.Specifier = L1[0];
			strncpy(EditCommand.Data, L1 + 1, strlen(L1));
		}
		EditCommands[i] = EditCommand;
		++i;
		++NumEdits;
	}
}
//---------------------------------------------------------------
void ReplaceText(EditCommand Edit, String Input)
{
	//----- Method used to find a substring and replace it with another substring, used in 'I' command
	String GetReplaced, Replacement, Output;
	char *Token;
	Token = strtok(Edit.Data, "/");
	if (Token != NULL)
	{
		strcpy(GetReplaced, Token);
		Token = strtok(NULL, "/");
	}
	if (Token != NULL)
	{
		strcpy(Replacement, Token);
	}
	Replacement[strlen(Replacement)] = '\0';
	GetReplaced[strlen(GetReplaced)] = '\0';
	int i = 0, j = 0, flag = 0, start = 0;

	//----- Check whether the substring to be replaced is present
	while (Input[i] != '\0')
	{
		if (Input[i] == GetReplaced[j])
		{
			if (!flag)
			{
				start = i;
			}
			j++;
			if (GetReplaced[j] == '\0')
			{
				break;
			}
			flag = 1;
		}
		else
		{
			flag = start = j = 0;
		}
		i++;
	}
	if (GetReplaced[j] == '\0' && flag)
	{
		for (i = 0; i < start; i++)
			Output[i] = Input[i];

		//----- Replace substring with replacement string
		for (j = 0; j < strlen(Replacement); j++)
		{
			Output[i] = Replacement[j];
			i++;
		}
		//----- Copy remaining portion of the input
		for (j = start + strlen(GetReplaced); j < strlen(Input); j++)
		{
			Output[i] = Input[j];
			i++;
		}
		//----- Copy the new string with the replacement to the input string so it is modified outside the method
		Output[i] = '\0';
		strcpy(Input, Output);
	}
}
int main(int argc, char *argv[])
{
	//----- Opening files and declaring variables needed later
	FILE *TextFile, *WriteFile;
	EditCommand EditCommands[MAX_COMMANDS];
	int i = 0;
	String Input, Output, Edited, OString, IString, AString, AddLine;
	int LineNum = 1;

	if (argc != 2)
	{
		printf("Enter 1 command line argument only\n");
		return (EXIT_FAILURE);
	}

	TextFile = fopen(argv[1], "r");
	WriteFile = fopen("output.txt", "w");
	if (TextFile == NULL)
	{
		printf("File not opened\n");
		exit(EXIT_FAILURE);
	}
	if (WriteFile == NULL)
	{
		printf("File not opened for writing");
		exit(EXIT_FAILURE);
	}
	//----- Calls FillArray method to get edit commands from the file the command line arg specifies
	FillArray(EditCommands, TextFile);
	fclose(TextFile);
	//----- Welcome message and exit condition
	printf("==========================================================\n");
	printf("Welcome to SVI Text Editor.\nEnter your text below, \"Exit Edit\" to close.\nEdited lines will be printed under the ones you've typed.\n");
	printf("Final text will be saved.\n");
	printf("==========================================================\n");
	while (fgets(Input, 256, stdin) != NULL)
	{
		int Deleted = 0;
		OString[0] = '\0';
		IString[0] = '\0';
		AString[0] = '\0';
		Input[strlen(Input) - 1] = '\0';

		//----- Upon exit, displays this message
		if (strstr(Input, "Exit Edit") != NULL)
		{
			printf("Exiting...\nEdited text saved to output.txt\n");
			fclose(WriteFile);
			exit(EXIT_SUCCESS);
		}

		for (int i = 0; i < NumEdits; ++i)
		{
			/*
			This part checks for the line range specification type first
			and for each type, it then goes through all the available commands
			and does the appropriate operations, and adds the changes to a string
			that has edits of only that type.
			*/
			if (EditCommands[i].LineSpecType == ALL_LINES)
			{
				if (EditCommands[i].Specifier == 'd')
				{
					sprintf(AddLine, "\n");
					Deleted = 1;
				}
				else if (EditCommands[i].Specifier == 'O')
				{
					strcat(OString, EditCommands[i].Data);
				}
				else if (EditCommands[i].Specifier == 's')
				{
					ReplaceText(EditCommands[i], Input);
				}
				else if (EditCommands[i].Specifier == 'I')
				{
					String InsertCopy;
					strncpy(InsertCopy, EditCommands[i].Data, strlen(EditCommands[i].Data));
					if (InsertCopy[strlen(InsertCopy) - 1] = '\n')
					{
						InsertCopy[strlen(InsertCopy) - 1] = '\0';
					}
					strcat(IString, InsertCopy);
				}
				else if (EditCommands[i].Specifier == 'A')
				{
					strcat(AString, EditCommands[i].Data);
				}
			}
			else if (EditCommands[i].LineSpecType == TEXT)
			{
				if (strstr(Input, EditCommands[i].LineType.PhraseSearch.KeyPhrase) != NULL)
				{
					if (EditCommands[i].Specifier == 'd')
					{
						sprintf(AddLine, "\n");
						Deleted = 1;
					}
					else if (EditCommands[i].Specifier == 'O')
					{
						strcat(OString, EditCommands[i].Data);
					}
					else if (EditCommands[i].Specifier == 's')
					{
						ReplaceText(EditCommands[i], Input);
					}
					else if (EditCommands[i].Specifier == 'I')
					{
						String InsertCopy;
						strncpy(InsertCopy, EditCommands[i].Data, strlen(EditCommands[i].Data));
						if (InsertCopy[strlen(InsertCopy) - 1] = '\n')
						{
							InsertCopy[strlen(InsertCopy) - 1] = '\0';
						}
						strcat(IString, InsertCopy);
					}
					else if (EditCommands[i].Specifier == 'A')
					{
						strcat(AString, EditCommands[i].Data);
					}
				}
			}
			else if (EditCommands[i].LineSpecType == LINENUM)
			{
				if (LineNum >= EditCommands[i].LineType.Range[0] && LineNum <= EditCommands[i].LineType.Range[1])
				{
					if (EditCommands[i].Specifier == 'd')
					{
						sprintf(AddLine, "\n");
						Deleted = 1;
					}
					else if (EditCommands[i].Specifier == 'O')
					{
						strcat(OString, EditCommands[i].Data);
					}
					else if (EditCommands[i].Specifier == 's')
					{
						ReplaceText(EditCommands[i], Input);
					}
					else if (EditCommands[i].Specifier == 'I')
					{
						String InsertCopy;
						strncpy(InsertCopy, EditCommands[i].Data, strlen(EditCommands[i].Data));
						if (InsertCopy[strlen(InsertCopy) - 1] = '\n')
						{
							InsertCopy[strlen(InsertCopy) - 1] = '\0';
						}
						strcat(IString, InsertCopy);
					}
					else if (EditCommands[i].Specifier == 'A')
					{
						strcat(AString, EditCommands[i].Data);
					}
				}
			}
		}
		/*
		After all the edits are done and copied to their respective strings, to print out
		the correct fully edited line, you print out the edits in the order that they are
		supposed to occur. So if deleted, only the 'O' command goes through, but otherwise
		the order is 'O', 'I', edited input, 'A'
		*/
		if (strlen(AString) < 2 && Input[strlen(Input) - 1] != '\n')
		{
			strncat(Input, "\n", 1);
		}
		if (Deleted)
		{
			printf("==========================================================\n");
			printf("%s", OString);
			printf("\n");
			printf("==========================================================\n");
		}
		else
		{
			fputs(OString, WriteFile);
			fputs(IString, WriteFile);
			fputs(Input, WriteFile);
			fputs(AString, WriteFile);
			printf("==========================================================\n");
			printf("%s%s%s%s", OString, IString, Input, AString);
			printf("==========================================================\n");
		}
		++LineNum;
	}
	return (EXIT_SUCCESS);
}
