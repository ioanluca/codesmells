## Detecting Code Smells

The aim of this assignment is for you to gain some experience of building a program analysis tool. Your task is to build and analyser to detect Code Smells (also known as "Bad Smells") in Java software systems using JavaParser and employing either static or dynamic analysis (or a combination of both).

This is to be carried out individually and will also contribute 20% to the mark for the class.

More details about code smells can be found in the following links:
<ul id="yui_3_17_2_1_1540078579889_165"><li><a href="https://sourcemaking.com/antipatterns/software-development-antipatterns" target="_blank">Software Development Antipatterns</a></li><li><a href="http://mikamantyla.eu/BadCodeSmellsTaxonomy.html" target="_blank">A Taxonomy for Bad Code Smells</a></li><li id="yui_3_17_2_1_1540078579889_164"><a href="https://blog.codinghorror.com/code-smells/" target="_blank">Code Smells</a></li></ul>

<table>
<caption style="caption-side: top;"><b>Code Smells</b></caption>
<thead>
<tr>
<th scope="col" style="border-width: 3px; border-style: solid;">Easy (up to 2 marks each)<br></th>
<th scope="col" style="border-width: 3px; border-style: solid;">&nbsp;</th><th scope="col" style="border-width: 3px; border-style: solid;">Medium (up to 5 marks each)<br></th>
<th scope="col" style="border-width: 3px; border-style: solid;">&nbsp;</th><th scope="col" style="border-width: 3px; border-style: solid;">Hard/Impossible (up to 10 marks each)<br></th>
</tr>
</thead>
<tbody>
<tr>
<td style="border-width: 3px; border-style: solid;"><i>Long Method</i></td>
<td style="border-width: 3px; border-style: solid;">&nbsp;</td><td style="border-width: 3px; border-style: solid;">Primitive Obsession</td>
<td style="border-width: 3px; border-style: solid;">&nbsp;</td><td style="border-width: 3px; border-style: solid;">Refused Bequest</td>
</tr>
<tr>
<td style="border-width: 3px; border-style: solid;"><i>Large Class</i></td>
<td style="border-width: 3px; border-style: solid;">&nbsp;</td><td style="border-width: 3px; border-style: solid;">Data Clumps</td>
<td style="border-width: 3px; border-style: solid;">&nbsp;</td><td style="border-width: 3px; border-style: solid;">Alternative 
		Classes with <br>Different Interfaces</td>
</tr>
<tr>
<td style="border-width: 3px; border-style: solid;">Long Parameter List</td>
<td style="border-width: 3px; border-style: solid;">&nbsp;</td><td style="border-width: 3px; border-style: solid;">Temporary Field</td>
<td style="border-width: 3px; border-style: solid;">&nbsp;</td><td style="border-width: 3px; border-style: solid;">Duplicate Code</td>
</tr>
<tr>
<td style="border-width: 3px; border-style: solid;">Switch Statements <br></td>
<td style="border-width: 3px; border-style: solid;">&nbsp;</td><td style="border-width: 3px; border-style: solid;">Lazy class</td>
<td style="border-width: 3px; border-style: solid;">&nbsp;</td><td style="border-width: 3px; border-style: solid;">Dead Code</td>
</tr>
<tr>
<td style="border-width: 3px; border-style: solid;"></td>
<td style="border-width: 3px; border-style: solid;">&nbsp;</td><td style="border-width: 3px; border-style: solid;">Data class</td>
<td style="border-width: 3px; border-style: solid;">&nbsp;</td><td style="border-width: 3px; border-style: solid;">Speculative Generality<br></td>
</tr><tr>
<td style="border-width: 3px; border-style: solid;">&nbsp;</td>
<td style="border-width: 3px; border-style: solid;">&nbsp;</td><td style="border-width: 3px; border-style: solid;">&nbsp;Message Chains</td>
<td style="border-width: 3px; border-style: solid;">&nbsp;</td><td style="border-width: 3px; border-style: solid;">Feature Envy<br></td>
</tr><tr>
<td style="border-width: 3px; border-style: solid;">&nbsp;</td>
<td style="border-width: 3px; border-style: solid;">&nbsp;</td><td style="border-width: 3px; border-style: solid;"><i>&nbsp;Large Class</i></td>
<td style="border-width: 3px; border-style: solid;">&nbsp;</td><td style="border-width: 3px; border-style: solid;">&nbsp;Inappropriate Intimacy</td>
</tr><tr>
<td style="border-width: 3px; border-style: solid;">&nbsp;</td>
<td style="border-width: 3px; border-style: solid;">&nbsp;</td><td style="border-width: 3px; border-style: solid;"><i>&nbsp;Long Method</i><br></td>
<td style="border-width: 3px; border-style: solid;">&nbsp;</td><td style="border-width: 3px; border-style: solid;">&nbsp;Middle 
		Man<br></td>
</tr>
</tbody>
</table>

I'm not expecting you to implement solutions for all of these! Far from it. And you can't score more than 20 anyway. 

If you are just looking for a pass in the assignment then correctly implementing the first four easy ones will achieve that. If you are looking for more marks then add in one or two of the more challenging ones. Some of the harder ones in particular are very tricky, so don't underestimate the amount of work they can take. I would suggest starting with some of the easier ones and then build things up from there.

A few comments on the code smells above:

### Easy
I've classified this group as easy since they main involve counting things which can be easily identified and don't involve much coding at all. They are not all as straightforward as they might first appear. For instance, how do you determine the length of a method (or class)? Number of lines of code is one option which is very easy but also not very accurate. Number of statements is more accurate but harder to calculate (which is why it also appears in the medium category). Also, at what value do you decide when a method is too long or a class too large? This is something which is usually defined within company standards, so to keep things simple for this exercise anything more than 10 statements is a long method, more than 5 parameters is a long parameter list, and a large class is one with more than 100 statements. For the switch statement the parameter must be a user-defined type (note this is much harder than I first imagined so would earn a few more marks).

### Medium
This group start to get a little trickier to spot and involve a bit more analysis. You will notice that I've also included Large Class in this group. This is a different interpretation of the problem, where the class is doing too much or has too many responsibilities, and is not just about the length of the class. The challenge here is thinking how you might identify what a class's responsibilities are. The opposite of this is the lazy class. 

### Hard/Impossible
- These either involve more analysis, typically between classes (e.g. Middle Man, Feature Envy, Inappropriate Intimacy or Refused Bequest),  are hard to specify and identify (Alternative Classes with Different Interfaces, and Speculative Generality), or are well-know hard problems (Duplicate Code and Dead Code). At first sight these last two may not sound too tough, but duplicate code may involve one or more statements, and these may not be syntactically identical (differing by simple aspects such as changes in variable names, to more complex ones such as the same functionality coded in a total different way). Similarly, Dead Code could be a method that is never called (relatively easy to detect), or could be a section of code that is controlled by a condition that can never evaluate to true (much harder to spot).
The marks awarded will reflect the completeness and accuracy of the solution.

###
Your submission needs to include the code (zipped up) along with a short report (I'm only after a few pages) by which includes:

#### An overview of the chosen problem(s).
- An outline of your solution and a high-level overview of your design (a class diagram is fine - just an indication of who you have structured your solution)
- Details of any important/interesting parts of your implementation.
- Results and evaluation - what it does and how well it does it. You should run your solution over the entire test system provided and summarise the outputs in terms of: a) which smells were accurately detected, b) which were missed, and c) what false positives (incorrectly flagged up by your system as smells) were identified. It is likely that will be the majority of the report.
- A statement of the score (out of 20) that you think the work deserves along with a short justification for this (a couple of sentences).
- You will also be required to demonstrate your system in week 6/7.

#### Additional Information on the marking scheme
There are three basic components to how your submission will be assessed: what it does, how well it does it, and how you went about doing it.

- What it does: Some tasks are more complicated than others (see the table above)
- How well it does it: This ranges from brilliantly - produces the correct results for all the time, to adequate - works reasonably well for a limited number of cases, to... well let's not aim for that. 
- How you went about doing it: This considers what facilities of the JavaParser framework you employed and also also the quality of your design.


A note about content versus visualisation: Don't worry about producing any kind of GUI for this: text output is absolutely fine.

