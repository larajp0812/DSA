Coursework Description
1. Introduction
This is a group coursework assignment focused on finding a route between any two stations on
sections of the Transport for London (TfL) overground and tube network systems. The
configuration of these networks may change due to incidents such as line closures or engineering
works, which require alternative routes to be found or introduce delays to certain journeys.
The group coursework consists of three main parts:
(a) Develop two versions of a program to manage sections of the TfL overground and tube
network systems. The program should allow for actions such as:
• Adding or removing journey time delays between stations.
• Closing or opening tracks between stations.
• Removing delays and finding a route between any two stations on the covered sections.
One version should be implemented using only hand-coded software, while the second
version can utilize any available software components as part of the .NET framework or
Java API.
(b) Produce a group mini-report that includes:
• A description of the design and software for both versions.
• Program testing and results.
• An explanation and analysis of the algorithms used.
• A benchmarking analysis and comparison of the two versions.
• Conclusions.
(c) Group presentation of the completed work, including a demonstration of the applications and
a Q&A session.
➢ Allocation of Students to Groups
1. The allocation of students into groups will be done as follows:
a. The module leader will determine the acceptable group sizes based on the
number of students in the module, typically 3 students per group.
b. All students must be part of a group; no student will be allowed to complete the
coursework individually.
c. Students may form groups themselves, with groups of 3 students agreeing to
work together.
d. The module leader will assign students to groups, either by placing all students
or those who have not yet joined a group into new or existing groups.
Page 3 of 10
e. In the case of disagreements or disputes, the module leader will mediate and
make the final decision.
2. Once assigned to a group, students must remain in that group for the duration of
the coursework.
➢ Group Working
All students in a group are expected to act professionally and actively contribute to all
group activities necessary to complete the coursework deliverables. Each student
must demonstrate their individual contribution to the assessment. Failure to do so will
result in a lower mark than the group’s overall assessment score. Specifically, students
who do not contribute to a coursework component will receive zero marks for that
component.
Groups should hold regular meetings with clear agendas and, at a minimum, record:
Attendees. Agreed actions, deadlines, and responsible individuals.
The meeting minutes should be shared with all group members. It is recommended to
use online tools (e.g., Trello, GitHub, Monday.com, etc.) for managing software
development projects, such as creating workflows, managing development tasks,
planning sprints, and tracking progress and completion stages—especially if the team
is working remotely. These tools can also help maintain a "group activity diary" to
track and document the team's activities and progress.
Any documents or program code produced should be stored in an online repository
accessible to all group members (e.g., One Drive, Google Drive, GitHub, or a similar
platform with basic version control).
Throughout the coursework, the module leader may request access to these
documents to review progress and provide feedback and guidance.
2. Description of Coursework Components
2.1 TfL's Overground Tube Network systems Information
The TfL application is required to model the following sections of the TfL overground and tube
network systems:
• all tube stations that are on the Circle line, e.g. Baker Street, Victoria.
• all tube stations that are within the Circle line, irrespective of the line they are on, e.g.
Oxford Circus (Bakerloo, Central, Victoria), Covent Garden (Piccadilly), etc;
• all tube stations that are on the Jubilee line, i.e. from Stanmore to Stratford.
• all tube stations that are on the Victoria line, i.e. from Walthamstow Central to Brixton;
• all overground stations that are on the North London overground line, i.e. from
Richmond to Stratford.
▪ Note: that you can ignore the Hammersmith and City, District or Metropolitan lines where
they run alongside the Circle line or identify station as being on these lines. For example,
Page 4 of 10
include Edgeware Road as just a Circle line station, not as a Hammersmith and City and
District line stations as well.
Your model of the above network should include the following information:
• connections in both directions between adjacent tube stations, and between adjacent
overground stations, e.g. on the Jubilee line: Bond Street to Baker Street and Baker Street
to Bond Street;
• the interchanges at a station, e.g. at Baker Street can change: Jubilee (Eastbound,
Westbound) to/from Circle (Inner, Outer);
• the inter-station journey times, e.g. Jubilee (Westbound): Bond Street to Baker Street 2.28
minutes;
• for a line interchange at a station, e.g. a Baker Street change from Jubilee (Westbound) to
Circle (Inner), adds 2 minutes to the journey time.
2.1.1 TfL sources of Information
Here are some useful references to help you collect the required information. The main Transport
for London site: https://tfl.gov.uk/. This is a link to an Information request about – TfL's Station to
Station Journey Times:
https://www.whatdotheyknow.com/request/station_to_station_journey_times
This also gives a full list of tube lines, stations and station to station times. For the North London
overground line it is possible to workout the Station to Station Journey Times using its Timetables
tool on the TfL site.
A general information site is: https://en.wikipedia.org/wiki/London_Overground
2.2 TfL Application Functionality
The TfL application should have the following main functions for the TfL track engineers and
customers:
(1) Allow engineers to manage sections of the TfL overground and tube network systems by
providing the following operations:
i. ii. iii. iv. Add or remove journey time delays on track sections between stations in one or both
directions. E.g. add delay of 5 minutes to Victoria south bound journey from Warren Street
to Oxford Circus.
Close or open track sections between stations in one or both directions, e.g. close Jubilee
(Eastbound) from Bond Street to Green Park
print out the list of closed track sections, e.g. print closed track sections: Jubilee
(Eastbound): Bond Street - Green Park - closed
print out the lists of delayed journey track sections, with normal time and delayed times,
e.g. print delayed track sections:
Victoria (Southbound): Warren Street to Oxford Circus-5 min delay
(2) Allow customers to find the fastest route in time between any two stations on the TfL systems
by providing the following operations:
Page 5 of 10
(i) Find a route by entering a start and end station and find the fastest route between
them, displaying the route as a list of stations and indicating any interchanges from one
line to the next, indicating the new line and direction.
For example, for a journey from Marble Arch to Great Portland Street the output would be
like the following:
Route: Marble Arch to Great Portland Street:
(1) Start: Marble Arch, Central (Eastbound)
(2) Central (Eastbound): Marble Arch to Bond Street 1.00min
(3) Change: Bond Street Central (Eastbound) to Jubilee(Westbound) 2.00min
(4) Jubilee (Westbound): Bond Street to Baker Street 2.28min
(5) Change: Baker Street Jubilee (Westbound) to Circle (Outer) 2.00min
(6) Circle (Outer): Baker Street to Great Portland Street 1.90min
(7) End: Great Portland Street, Circle (Outer)
Total Journey Time: 9.18 minutes
(ii) Display information about a tube or overground station, similar to that available from
the TfL web site. (See Tutorial exercises.)
(3) Provide a basic terminal-based user interface in the form of a simple text-based menu that
allows a user to select one of the above operations from a list of options. It should verify that the
data input is correct, perform the operation and then print the output for the operation or a
confirmation message that the operation has been performed or could not be performed and the
reason why.
2.3 Two versions of the TfL application
Two versions of the TfL application should be developed:
Version 1: Handed coded using core C# or java classes.
This solution relies solely on hand-coded software and only utilizes the basic built-in data types
and classes available in C# or Java. For example, you cannot use any software from namespaces
like System.Collections.Generic or Microsoft.VisualStudio.GraphModel,
nor can you adapt third-party APIs such as com.google.common.collect.Multimap.
[Marks 45]
Version 2: Modified version of 1 replacing code with .NET or Java library classes
This version is based on Version 1, but has been modified by replacing any hand-coded data
structures or algorithms used in Version 1 with software available as part of the .NET Framework
Library or in the Microsoft.VisualStudio.GraphModel namespace or Java Library classes, among
others. However, any such software must be clearly identified and referenced in the CourseworkPage 6 of 10
Report. You are free to reuse as much basic code from your Version 1 application (e.g., user
interface, data input code, etc.) as needed, but you should make use of software from the .NET
Framework Library or any other standard library wherever applicable. [Marks 20]
2.4 Report
Produce a group mini-report of roughly 15 – 20 pages that provides a description, explanation,
justification and conclusions of the group's work. The report should contain sections on the
following topics:
1. Title page, module code and name, date, group identity and members.
2. Introduction to the work carried out, outlining which components of the coursework
have or have not been completed, known bugs in the systems, a summary of each group
member's contribution to the coursework.
3. Description of the software design of the 2 versions, e.g. using UML class diagrams for
each version, etc.
4. Brief description of classes, and data structures used, e.g. diagrams illustrating structure
and data stored in the more complex ones used.
5. Description and evidence of program testing, type of testing and examples of testing
cases.
6. Analysis of the main algorithms used, e.g. Big-O, a benchmarking comparison and
analysis of the two versions, conclusions.
7. Conclusions
8. References of the sources of information used for data about TfL, data structures,
algorithms analysis and software packages used in version 2 of the application.
The report should have the style and format of a formal technical document, it should be well
organized, and its contents should be presented in a professional manner. For example, a
contents page, page numbering, numbered sections and subsections, all figures and tables
numbered and labelled, etc. [Marks 20]
2.5. Group Presentation and Demonstration
After the coursework has been submitted, all groups must present their work to the module
leader and their peers. All students are required to participate in their group's presentation,
which includes:
• Presenting the group's work using slides,
• Demonstrating both versions of the application software,
• Participating in a Q&A session at the end of the presentation.
The presentations will take place after the submission deadline. The specific dates and times for
the presentation sessions will be confirmed prior to the coursework submission date [Marks 15]
Page 7 of 10
3 Coursework Deliverables
➢ Electronic files: Submit the following items to Blackboard by the submission deadline:
1. Software developed: C# or Java project(s) for the two versions of the TfL
application (Lo 2,3 4),
2. Sample output: Two text files, V1_output.txt and V2_output.txt, containing the
output for both versions (Lo 2,3 4).
3. 4. Group report: A PDF file containing the group report.
Presentation slides: The slides for the group presentation (Lo5).
All files should be compressed into a single ZIP archive. The archive must be named using
your student ID, group ID, and "CW", e.g., "StudentID_GroupID_CW.zip".
➢ Group presentation:
All students in each group must participate in the group’s presentation, software
demonstration, and Q&A session. These will take place after the coursework submission
deadline. The exact times and dates for the presentations will be confirmed by the
submission deadline.
❖ Coursework Demonstration
In accordance with the "ChatGPT in Computing Education: A Policy White Paper" and
university regulations, the lecturer/tutor will conduct a coursework demonstration during
the lab sessions. Attendance at this demonstration is mandatory. If you fail to attend, the
15% allocated for the demonstration will not be marked. During the demonstration, the
tutor will ask you to showcase your program and respond to questions regarding your
code and report.
Page 8 of 10
Coursework Marking scheme
The Coursework will be marked based on the following component marking criteria
Marking criteria Mark per
component
Mark Comments
Two Software Applications 65
Report 20
Group Presentation and
Demonstration
15
Total 100
1. TfL Application Version 1 – 45%
Application Component Comment (if appropriate) Mark
Add/Remove track section delays / 2
Close/Open track sections / 2
Fastest route between 2 stations / 20
Printing:
closed track
sections track
section delays
station information
/ 3
Terminal UI / 2
Data Structures / 5
Algorithms / 5
Code Structure & Readability / 4
Sample output / 2
TOTAL / 45
2. TfL Application Version 2 – 20%
Application Component Comment (if appropriate) Mark
Version 1 Functionality / 5
Modified Data Structures &
Algorithms
/ 10
Code Structure & Readability / 3
Page 9 of 10
Sample output TOTAL 3. Group Report – 20%
Report Components Introduction Software design & description Program testing Algorithms analysis Conclusions References,
Report presentation TOTAL 4. Group Presentation - 15%
Presentation Component Slides Contents Quality of Presentation Software Demonstration Q & A Session TOTAL / 2
/ 20
Comment (if appropriate) Mark
/ 2
/ 6
/ 4
/ 4
/ 2
/ 2
/ 20
Comment (if appropriate)
/ 3
/ 5
/ 4
/ 3
/ 15