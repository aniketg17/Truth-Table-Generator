# Propositional Logic Expression Solver

## Overview
This is a Java application to parse and solve propositional logic expressions and produce their truth tables. I have used JavaFX for the GUI.

## Motivation
Inspired from my discrete math class, I attempted to make this program. My purpose of developing this application was for me and my peers to be able to verify our solutions while simultaneously learning how to programmatically implement what we were learning. Parsing expressions and dealing with logical operations deepened my understanding of propositional logic and discrete math as a whole. 

## Steps to run the project
1. Compile and run the project using command line or some IDE (eg. IntelliJ)
2. Fill in a well-formed formula (WFF) of propositional logic. The logical expressions that the program parses are the following:
 
    a) Implication: ⇒ , →
    
    b) Biconditional: ⇔ ,  ≡ , ↔
    
    c) Negation: ¬ ,  ˜  , !
    
    d) Conjunction (and): ∧ , &  , ·
    
    e) Disjunction (or): ∨ , + , ∥
    
    f) Exclusive Or (xor): ⊕ , ⊻
    
    g) Brackets: ( , [  ,  {
    
    Note: The symbols are delimited by ' , '.

## Demo
Below are two sample expressions being parsed and solved to be converted into truth tables. 
In case it is unclear, the first expression is '((p∧q)∨(s∧r))' and the second expression is '¬(p ⇔ (r∨s))'.

![ezgif-5-cf628f431fc7](https://user-images.githubusercontent.com/54602672/90309078-902cb880-df02-11ea-9d4b-ea059dde7f32.gif)

![ezgif-5-a1fb7032e982](https://user-images.githubusercontent.com/54602672/90309079-91f67c00-df02-11ea-83f7-bffe477a6b7a.gif)
 
