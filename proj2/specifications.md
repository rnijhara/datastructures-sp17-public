# Project 2: Database, version 1.0 #
 
### Table of Contents
* Overview
* Design Process
* Commands
* Definitions
* Special Values
* Joins
* TBL Files
* Example
* Your Task
* Sample Code
* Acknowledgements

* * *
### Overview ###
In this project, you'll be building a small version of what is called a relational database management system (DBMS), as well as a DSL (Domain Specific Language) with which a user can interact with your database. The language you will be implementing is similar to the declarative programming language SQL, which is used in the real world to interact with and query databases, large or small. Below is an example of a small database, with 3 tables, 'fans', 'teams', and 'records'.

**fans**

**Lastname string**	| **Firstname string** |	**TeamName string**
--- | --- | --- 
Lee	| Maurice	| Mets
Lee	| Maurice	| Steelers
Ray	| Mitas	| Patriots
Hwang	| Alex |	Cloud9
Rulison	| Jared |	EnVyUs
Fang | Vivian	| Golden Bears

**teams**

**TeamName string** |	**City string** |	**Sport string**	| **YearEstablished int** |	**Mascot string** |	**Stadium string**
--- | --- | --- | --- | --- | ---
Mets	|	New York	|	MLB Baseball	|	1962	|	Mr. Met	|	Citi Field
Steelers	|	Pittsburgh	|	NFL Football	|	1933	|	Steely McBeam |	Heinz Field
Patriots	|	New England	|	NFL Football	|	1960	|	Pat Patriot	|	Gillette Stadium
Cloud9	|	Los Angeles	|	eSports	|	2012	|	NOVALUE	|	NOVALUE
EnVyUs	|	Charlotte	|	eSports	|	2007	|	NOVALUE	|	NOVALUE
Golden Bears	|	Berkeley	|	NCAA Football	|	1886	|	Oski	|	Memorial Stadium

**records**

**TeamName string**	| **Season int**	| **Wins int**	| **Losses int**	| **Ties int**
--- | --- | --- | --- | --- 
Golden Bears	| 2016	| 5	| 7	| 0
Golden Bears	| 2015	| 8	| 5	| 0
Golden Bears	| 2014	| 5	| 7	| 0
Steelers	| 2015	| 10	| 6	| 0
Steelers	| 2014	| 11	| 5	| 0
Steelers	| 2013	| 8	| 8	| 0
Mets	| 2015	| 90	| 72	| 0
Mets	| 2014	| 79	| 83	| 0
Mets	| 2013	| 74	| 88	| 0
Patriots	| 2015	| 12	| 4	| 0
Patriots	| 2014	| 12	| 4	| 0
Patriots	| 2013	| 12	| 4	| 0

* * *
### Design Process ###
Because of the size of this project, before you start coding you'll form groups to think about the high level design of your project. In the labs from Feb 16-17, you'll form groups of 4 and spend the next week working together to create a design for your database implementation. You should use this time to also try some exploratory programming to try out some of your ideas on small, toy examples.

This design time should cumulate into a design document, a short outline of your design, its advantages, disadvantages, etc. The format of your design doc is open ended, as you will not be turning it in for a grade. Instead, you'll be presenting your ideas to your lab the following week (Feb 23-24) for a chance to receive constructive feedback and see others' designs.

After brainstorming your design, presenting it, and getting feedback, your 4 person design group will split into two 2 person independent coding teams (ICTs) in order to actually complete the project. Both ICTs may use the design that they came up with together and discuss said design, but they may not do any of the following:

* Share implementation code
* Look at the other group's code
* Share test code

These two ICTs will submit separate solutions to the project. However, after the project deadline, the two ICTs will come back together and review the other team's code, providing feedback on their implementation. This feedback can be about their code's composition, simplicity, correctness, consistency and/or efficiency as well as their git history.

This process will be covered in more detail during lab. Design groups of 2 or 3 students are also allowable, though every design must split into two ICTs and no ICT can have more than 2 people.
* * *
### Commands ###
There are several commands you'll have to support in your database implementation. They each have specific error conditions but there are some errors that multiple share. When relevant, this includes malformed commands, commands that result in illegal operations within the database and commands involving tables that don't exist (except create table). Any command that errors should result in no change to the database.

##### Create Table #####
There are two variants of the `create table` command, explained below.
```
create table <table name> (<column0 name> <type0>, <column1 name> <type1>, ...)
```
Create a table with the given name. The names and types of the columns of the new table are supplied in a parenthesized list, in order. This defines the column order for this table.
```
create table <table name> as <select clause>
```
Create a table with the given name. The columns, content and types of columns of the table are those of the intermediate table created by the result of executing the select clause.

It is an error to create a table with no columns and it is also an error to create a table that already exists.

Create Table should return the empty String on success, or an appropriate error message otherwise.

#### Load ####
```
load <table name>
```
Load the table stored in the file `<table name>.tbl` into memory, giving it the name `<table name>`. The row order of the table is defined as the order in which the rows are listed in the TBL file. If a table with the same name already exists, it should be replaced. If the relevant table file is an invalid table, it is an error.

Load should return the empty String on success, or an appropriate error message otherwise.

#### Store ####
```
store <table name>
```

Write the contents of a database table to the file `<table name>.tbl`. If the TBL file already exists, it should be overwritten.

Store should return the empty String on success, or an appropriate error message otherwise.

#### Drop Table ####
```
drop table <table name>
```
Delete the table from the database.

Drop Table should return the empty String on success, or an appropriate error message otherwise.

#### Insert Into ####
```
insert into <table name> values <literal0>,<literal1>,...
```

Insert the given row (the list of literals) to the named table. The table must already be in the DB and the provided values must match the columns of that table. If a provided value cannot be parsed into the type of the column it is listed in, it is an error. The given row is appended to the table, becoming the last row in its row order.

It is an error to insert a row that does not match the given table.

Insert Into should return the empty String on success, or an appropriate error message otherwise.

#### Print ####
```
print <table name>
```
Print should return the String representation of the table, or an appropriate error message otherwise.

#### Select ####
Select statements are used to extract data from the database in a programmatic fashion. Instead of simply writing to and printing individual tables, select statements allow you to form more complicated requests. They take the form below:
```
select <column expr0>,<column expr1>,... from <table0>,<table1>,... where <cond0> and <cond1> and ...
```
The result of a select statement is a new table that has been formed from the join of the given table(s), filtered by the conditional statement(s), and selected from with the column expression(s). The order in which these operations happen is up to your implementation, as long as the output is correct. The joining of tables is optional, i.e. selecting from a single table is valid. The conditional statements are also optional, so a select could be as simple as:
```
select <column expr> from <table0>
```
The order of the columns in the new table is defined by the order they are listed in the select. In the case that all columns are selected (with the `*` operator), the order is defined by the column order for the join.

It is an error to write a select statement that involved no columns of the listed tables.

Select should return the String representation of the produced table, or an appropriate error message otherwise.
* * *
### A Note on Whitespace ###
Arbitrary amounts of whitespace are allowed to exist within a command, as long as they delimit parts of the command. That is, arbitrary whitespace may exist between column names, operators, keywords, etc. As an example, the following queries are all valid and equivalent:
```
select a,b from table1, table2
select      a,b from table1, table2
select a , b from table1    ,table2
select a,         b    from table1,table2
         select a   , b from table1,table2
```

While whitespace should not matter in commands, it does matter in your output. There should be no spaces between fields in your output format. The only spaces should be inside the quotes of string values, and a single space in between a column name and its type.
* * *
### Definitions ###
#### Names ####
Table and column names are non-empty sequences of characters. They may contain only letters, numbers and underscores, and must start with a letter. In addition, no keywords (words involved in commands) may be used as table names, but you don't have to handle this case.

#### Literals ####
A literal is a non-empty sequence of characters, and may not contain newlines, tabs, commas, or quotes of any kind. A string type literal is denoted by surrounding the characters with single quotes, and int and float types are denoted by a non-quoted set of characters.

#### Types ####
The type of a column is the class of data it can hold. The possible types are `string`, `int` and `float`. They should not be quoted when specified, and are case sensitive (i.e. fLoaT is not a valid type).

String types are defined as a sequence of characters surrounded by single quotes. They may not contain newlines, tabs, commas, or quotes of any kind. You do not have to handle the case where a string contains any of these illegal characters. The string representation of a string is its contents surrounded by single quotes.

Int types are defined as a sequence of the decimal digits 0-9, without any other characters. They are not quoted. The string representation of an int is simply the number written in base 10.

Float types are defined as a sequence of the decimal digits 0-9, with exactly one `.` character present. The `.` may be the first character, last character, or anywhere in the middle. You do not have to handle other ways of writing floating point numbers like scientific notation. When printing float types, you should specify them to exactly 3 decimal places. The string representation of a float is simply the number written in base 10 to 3 decimal places.

Both int and float types may optionally start with a `-` character, denoting that it is negative.

#### Column Expressions ####
A column expression is an expression of the form `<operand0> <arithmetic operator> <operand1> as <column alias>,` or it may just be a single operand. There are a few special cases for column expressions, listed below.

* If a lone `*` is supplied instead of a list of column expressions, all columns of the result of the join should be selected.
* If only a single operand is given, it must be a column name. The new column shares its name with the original column.
*If two operands are given, the left must be a column name while the right could be either a column name or a literal. An alias must always be provided when there are two operands.

If a column is created as the result of a column expression that was not just a column name, the name of the new column is given by the as keyword. You do not have to handle the case where a select statement creates duplicate columns.

Columns that are created in a column expression may not be used in a later column expression in the same select. That is, a select like the following is not allowed:
```
select x + y as a, a * 2 from points
```
You may assume that column expressions will be at most binary. That is, a select like the following will not be tested:
```
select x + y + z from points3D
```
#### Operands ####
Valid operands are column names and literals.

#### Arithmetic Operators ####
Valid arithmetic operators are `+`, `-`, `*`, and `/` for int and float types. For strings, the only allowed operation is concatenation, which is represented by the `+` operator.

If one operand is an int, and one is a float, the resulting type is a float.

It is an error to try and perform operations where one operand is a string, and the other is an int or float.

#### Conditional Statements ####
A condition statement is a comparison of rows in the given tables. There are two kinds of conditions: unary and binary. Unary conditions are of the form `<column name> <comparison> <literal>`, while binary conditions are of the form `<column0 name> <comparison> <column1 name>`. The difference is that unary conditions involve only one column, while binary conditions involve two columns. You may assume that in a unary condition, the literal is always the right operand.

In order to be included in the resulting table of a select statement, a row must pass all conditional statements listed in the select. For example, if we do `select * from t1 where y > 5 and x > 4`, we will return only rows that match both of these conditions.

Special note: A conditional statement may only use columns that exist after:

1. Joins (if any) have been evaluated, e.g. `select * from t1, t2`
2. Column expressions have been evaluated, e.g. `select first + last as whole from t1`
As an example of a disallowed selection, the select statement below is not allowed and will not be tested:
```
select first + last as whole from names where last > 'Smith'
```

The expected output from the statement above can still be achieved though, with two statements:
```
create table afterSmith as select * from names where last > 'Smith'
select first + last as whole from afterSmith
```
#### Comparison Operators ####
Valid conditional comparators are `==`, `!=`, `<`, `>`, `<=` and `>=`. These behave the same way that they do in the Java programming language, allowing for translation to the proper `compareTo` constructs for Strings.

It is an error to compare strings to either int or float types, but it is valid to compare an int and a float.

#### Table String Representation ####
The String representation of a table is its columns and rows in CSV (comma separated value) format, each on a separate line. The first line of the String should be a comma separated list of the column names and types, in the form `name type,name type,...`. Successive lines should each be an individual row of the table, with each row listed as a comma separated list of the String representations of its entries. There should be no whitespace between a value and the commas around it. The order in which the rows are printed should be the same as their order in the table, as defined by the row order.

For example, the string representation of the fans table is below:
```
Lastname string,Firstname string,TeamName string
'Lee','Maurice','Mets'
'Lee','Maurice','Steelers'
'Ray','Mitas','Patriots'
'Hwang','Alex','Cloud9'
'Rulison','Jared','EnVyUs'
'Fang','Vivian','Golden Bears'
```
* * *
### Special Values ###
There are two different kinds of special values you'll have to handle in your implementation. These are NaN and NOVALUE.

#### NaN ####
If a column expression results in a divide by zero error, the value in that row should be the string "NaN", unquoted. A NaN value has the same type as the column it exists in.

Any arithmetic operation that has a NaN value as one of its operands should produce a NaN. Any comparison operators involving NaN should treat it as being larger than all other values except itself, to which it should be equal. NaN may not be given as a literal in a column expression or an insert. You do not have to handle this.

#### NOVALUE ####
The special value NOVALUE is exactly what it sounds like, it represents the absence of a value. The value present in a database row should be the string "NOVALUE", unquoted. A NOVALUE value has the same type as the column it exists in.

Any arithmetic operation that has a NOVALUE as one of its operands should treat it as the zero value for the column type. The zero values for strings, ints and floats are '', 0 and 0.0, respectively. The one exception to this is when both operands to arithmetic are NOVALUE, in this case the result should also be NOVALUE. Any comparison operation that has a NOVALUE as one of its operands should evaluate to false. NOVALUE may not be given as a literal in a column expression. You do not have to handle this.
* * *
### Joins ###
A join is just what it sounds like, it combines two tables. The kind of join you'll be implementing is called a natural inner join. In a natural inner join, the new table's rows are formed by merging pairs of rows from the input tables. Two rows should be merged if and only if all of their shared columns have the same values. In the case that the input tables have no columns in common, the resulting table is what is called the *Cartesian Product* of the tables. That is, each row of table A is considered to match each row of table B as if they had a column in common.

With these rules, you can imagine that it is possible to concoct a join that results in a table with two columns that have the same name, but different types. Luckily for you, you don't have to handle this case.

It is possible to join multiple tables. For example, to join tables A, B and C, you would join A with B, and then join the result of that with C. This rule can be generalized to as many tables as desired, joining them from left to right.

The column order of a join is defined as follows:

* All shared columns come first, in the relative order they were in the left table of the join (the one listed first in the select clause)
* The unshared columns from the left table come next, in the same relative order they were before.
* The unshared columns from the right table come last, in the same relative order they were before.

The row order of a join is a little more subtle. In short, all rows of the left table should remain in the same relative order that they were in before the join. Then, within a match to a row of the left table, all rows of the right table should be in the same relative order. An example might help. Let us suppose that we are joining tables `A` and `B`. When joining, it turns out that row `a0` matches rows `b0` and `b2` and row `a1` matches row `b1`. The order of the rows in the output should be `merge(a0,b0)`, `merge(a0,b2)`, `merge(a1,b1)`. Note that even though `b1` comes before `b2` in the original table `B`, their order is swapped in the output. This is because `A` is the left table, and since `b2` matched with `a0` (which comes before `a1`), it shows up first since the row order of the left table takes priority.

Lets look at some examples of joins. Suppose we had the two tables below:

**t1**

**X int** |	**Y int**
--- | ---
2	| 5
8	| 3
13	| 7

**t2**

**X int** | **Z int**
--- | ---
2	| 4 
8	| 9
10	| 1

These two tables have a single column in common: X. Thus when we join them, any rows that match in the X column will be merged together. Any rows that have no matches will be discarded. Now suppose we execute the command `create table t3 as select * from t1, t2`. This command says to create a new table named t3 that is the result of selecting all columns from the join of tables `t1` and `t2`. The table `t3` is show below.

**t3**

**X int** |	**Y int** |	**Z int**
--- | --- | ---
2	| 5	| 4
8	| 3	| 9

Notice how the row with `X=13` from `t1` and the row with `X=10` from `t2` are both missing. This is because they did not match any rows in the other table.

Let's look at an example where two tables had no columns in common. Suppose we added the table below to our database.

**t4**

**A int** |	**B int**
--- | ---
7	| 0
2	| 8

Now lets say we execute the command `create table t5 as select * from t3, t4`. The resulting table looks like this:

**t5**

**X int** |	**Y int** |	**Z int** |	**A int** |	**B int**
--- | --- | --- | --- | ---
2	| 5	| 4	| 7	| 0
2	| 5	| 4	| 2	| 8
8	| 3	| 9	| 7	| 0
8	| 3	| 9	| 2	| 8

Since there were no columns in common, the result of the join was the cartesian product of the two tables, with every row in `t3` being matched to every row in `t4.`

For more information about joins, you can google around for 'natural inner join' and read up on how SQL natural joins work; ours functions the same way.
* * *
### TBL Files ###
The format of .tbl files is exactly the same as the table string representation, described in the Definitions. For a given table, the TBL file it should be stored in is <name>.tbl, where <name> is the name of the table.
* * *
### Example ###
Suppose that we have stored the tables given at the start of the spec into appropriately named TBL files. Below is a possible transcript of an interaction with an instance of the database. A line starting with '> ' indicates that what follows is a String passed to the database's transact function, which will be followed by successive lines displaying the String returned by the call to transact. Lines starting with '# ' are comments, and are just for the sake of explaining the transcript.
```
> load fans
> load teams
> load records
> load badTable
ERROR: TBL file not found: badTable.tbl
> print fans
Lastname string,Firstname string,TeamName string
'Lee','Maurice','Mets'
'Lee','Maurice','Steelers'
'Ray','Mitas','Patriots'
'Hwang','Alex','Cloud9'
'Rulison','Jared','EnVyUs'
'Fang','Vivian','Golden Bears'
# Find all fans who's last name comes after 'Lee', and their favorite teams
> select Firstname,Lastname,TeamName from fans where Lastname >= 'Lee'
Firstname string,Lastname string,TeamName string
'Maurice','Lee','Mets'
'Maurice','Lee','Steelers'
'Mitas','Ray','Patriots'
'Jared','Rulison','EnVyUs'
# Find all the mascots for teams younger than 75 years
> select Mascot,YearEstablished from teams where YearEstablished > 1942
Mascot string,YearEstablished int
'Mr. Met',1962
'Pat Patriot',1960
NOVALUE,2012
NOVALUE,2007
# Find all the seasons in which a sports team did poorly, and the city in which they were based
> create table seasonRatios as select City,Season,Wins/Losses as Ratio from teams,records
> print seasonRatios
City string,Season int,Ratio int
'New York',2015,1
'New York',2014,0
'New York',2013,0
'Pittsburgh',2015,1
'Pittsburgh',2014,2
'Pittsburgh',2013,1
'New England',2015,3
'New England',2014,3
'New England',2013,3
'Berkeley',2016,0
'Berkeley',2015,1
'Berkeley',2014,0
> select City,Season,Ratio from seasonRatios where Ratio < 1
City string,Season int,Ratio int
'New York',2014,0
'New York',2013,0
'Berkeley',2016,0
'Berkeley',2014,0
> store seasonRatios
> store badTable
ERROR: No such table: badTable
```
* * * 
Your Task
You are responsible for implementing the behavior described in the spec. Your provided skeleton code is as follows:

```java
package db;

public class Database {
    public Database() {
        // YOUR CODE HERE
    }

    public String transact(String query) {
        return "YOUR CODE HERE";
    }
}
```

You may not change the interface defined by this skeleton code. Your Database class must have a public zero argument constructor and a public transact method as defined above. The transact method takes in a string, to be treated as a query command, and returns a string: the output of executing that command on the database. It is fine to add more methods and fields though. Additionally, you may create as many of your own classes as you would like, but they must be part of the `db` package.

A note on your database's functionality: it is very important that your implementation of print and load work correctly! We will be using them to test many other functions of your project as it is the simplest way to quickly create a table and see its contents. A solution that has bugs in either of these functions will receive very few points, regardless of the quality of the rest of the implementation.

You must not allow your program to crash as the result of bad input (or incorrect code that causes exceptions). If a query is malformed or attempts to perform an illegal operation (such as add an int and a String), an appropriate error message should be returned to the user. These error messages must take the form `ERROR: <relevant message here>`. It does not matter exactly what the relevant message is, but for your own debugging purposes, good messages would help.

You may use any classes from the following standard library packages:

* `java.util`
* `java.util.regex`
* `java.io`
* `java.nio`

You may also use any packages provided in the skeleton javalib folder, including `org.JUnit` and `jh61b`.

If you use any packages outside of these or the provided code, your project will receive zero points.

If you feel you should be able to use a standard library class that is not included in these packages, ask on Piazza. It is unlikely, but possible. You may **NOT** use any third party libraries, e.g. Apache Commons library.
* * *
### Sample Code for Parsing and Interactivity ###
When it comes time to build the interface to your database, you'll find that processing user input is a significant challenge. We've provided two classes `Main` and `Parse` that you will find helpful for setting up your interface. Particularly strong programmers might consider looking at these classes.

`Main.java` is a simple REPL client for your database. It will create an instance of your database and pass whatever you type at its prompt to the transact function, displaying the return value. This will come in handy for prototyping features and ideas, but we still recommend that you write your own unit tests, since any tests you perform with this class will disappear once you exit it.

`Parse.java` provides examples of how one can process the inputs from the user. This class is capable of parsing the commands your database should accept and displaying a brief summary of the command. Try it out and see what happens, but be aware that it is not industrial strength. It will check the general form of commands to detect malformed queries, but it is still possible to create malformed commands that it accepts. How you handle malformed commands such as these in your own implementation is up to you, but we recommend you do it somewhere other than your parser, as detecting some errors at such a high level is quite complicated. Feel free to take whatever code you would like from this class to parse commands passed to your database's transact command. **You should not be directly calling any methods from Parse in your project, but instead using its logic as a reference.**
* * *
### Acknowledgements ###
Thanks to all of the CS 61B staff for helping test this project, and special thanks to Aidan Clark, Eli Lipsitz, Kevin Lowe and Jared Rulison for taking it on when the spec was still in early beta.
