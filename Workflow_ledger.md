# TeamG-CooksNest

## CooksNest

### Login Information:

1. Vasant
Email: vabhard@hotmail.com
password: cyril101

2. George (Fictional)
Email: ace@yahoo.com
Password: Abcde123

## TeamG-CooksNest Meeting Notes

### Sunday, March 21: Begin Sprint 1!
Start Time: 2:45 PM EST
End Time: 3:30 PM EST
Attendance: All Team Members
1. Onboarded all team members onto git repository
2. Prepped Github project board with Sprint 1 tasks
3. Began to do initial Firebase research

**Tasks Assigned**
- Anish: Research feature code (profile toggle, swipe screen, etc)
- Ryunosuke: Set up Firebase Database
- Vasant: Finalize Figma design and prep UI/UX look
- Abhi: Research and set up Firestore Authentication

### Wednesday, March 24: Progress Check
Start Time: 7 PM
End Time: 7:30 PM
Attendance: Ryunosuke, Vasant, Abhi
1. Checked in to see what progress each team member made
2. Made edits to Firebase set-up
3. Did research to resolve Firebase errors that Ryunosuke was getting

**Tasks Assigned**
- No new tasks.

### Thursday, March 25: Meeting with Max
Start Time: 8 PM EST
End Time: 9 PM EST
Attendance: All Team Members
1. Clarified Team Project Part IV (Sprint 1) description
2. Fixed Ryunosuke's Firebase errors with Max's help

**Tasks Assigned**
- Ryunosuke: work with Abhi to set up Firestore Authentication

### Friday, March 26: Progress Check
Start Time: 3:00 PM EST
End Time: 3:45 PM EST
Attendance: All Team Members
1. With Firebase and Firestore successfully working, we decided to plan which features each team member would begin work on over the weekend (Sunday was busy for several team members so we decided to cancel Sunday's meeting)
2. Fixed Abhi and Ryunosuke's errors with Firestore authentication -> authentication works now!
3. Worked on some bugs that Vasant was getting with his Github, which ended up being a Gradle issue
4. Helped Anish with some issues with Firebase connection and sync
5. Finally, we assigned a single feature for each team member

**Tasks Assigned**
- Anish: Work on swipe screen (gesture detection, link to "save recipe")
- Ryunosuke: Work on Add Recipe
- Vasant: Work on UI/layout for Profile, Browse, and Add Recipe
- Abhi: Link back-end database to recipe list (posted recipes go in "My Posted Recipes" on Profile, for example)

### Sunday, March 28: Progress Check
1. Cancelled meeting due to conflicts for some team members

**Tasks Assigned**
- No new tasks.

### Wednesday, March 31: Progress Check
Start Time: 7:00 PM EST
End Time: 8:00 PM EST
Attendance: Anish, Ryunosuke, Vasant
1. Checked in to see how team members were doing for features, and did a general pass over our app to see how it looks right now
2. Fixed an issue Ryuno was having with the added recipes by hardcoding some recipes to test
3. Ryuno was able to complete the Add Recipe page, but setting an Image in Add Recipe was not working for him
4. Worked on some issues with ListView that Vasant was having, we were unable to fix the issue
5. Anish was having errors with the swipe screen, but we were unable to fix the issues
6. Anish and Ryunosuke decided to swap tasks to see if fresh eyes could solve the issues

**Tasks Assigned** 
- Anish: Image set and get for Add Recipe
- Ryunosuke: Browse page should recognize left and right gestures for swiping

### Thursday, April 1: Meeting with Max
Start Time: 8 PM EST
End Time: 9 PM EST
Attendance: All Team Members
1. Filled Max in about current issues we were having after last night's meeting
2. Max was able to provide some advice for the ListView issue
3. Max offered to take a closer look at the swiping issue that Anish and Ryunosuke were having
4. After meeting with Max, the team met to hammer our issues with ListView, using some of Max's advice
5. We also noticed a helpful Piazza post about swiping and resolved to follow its directions to help recognize swipe gestures on screen

**Tasks Assigned**
- Ryunosuke: Follow Piazza post to fix swipe issues

### Friday, April 2: Progress Check
Start Time: 3:30 PM EST
End Time: 4:50 PM EST
Attendance: Anish, Vasant, Abhi, Ryunosuke (later)
1. Finished deleting recipes from device and Firebase
2. Images can be set in the Add Recipes page, but Anish was having trouble pulling the images from Firebase

**Tasks Assigned**
- Anish: Set up Firebase Cloud Storage to store images separately
- Ryunosuke: Work on View and Edit Recipe from Profile, work on posted/saved toggle in Profile
- Vasant: Continue UI work for Browse and Profile
- Abhi: Work on pulling recipes from Firebase to Profile and separate user's posted recipes from their saved recipes

### Saturday, April 3: Extra Team Day
Start Time: 12 PM EST
End Time: 2 AM EST
Attendance: All Team Members (in and out for various hours)
1. Met for extra day before submission deadline to hammer out last minute issues
2. Anish was able to set up separate image Cloud Storage and linked it to recipes stored in Firebase database
3. Ryunosuke was able to set up toggler in Profile, but had issues with passing Intent from RecipeAdapter
4. Vasant was able to make major changes to Browse, Profile, and Add Recipe UI, but had issues with layout formatting
5. Abhi was able to query recipes from database to profile and browse

**Tasks Assigned**
- Anish: Work on pulling UUID from database to call image path from cloud storage
- Ryunosuke: Fix issues with "edit recipe" in Profile (Intent errors)
- Vasant: Fix issues with layout formatting
- Abhi: Debug issues with Browse page and swipe screen

### Sunday, April 4: Recording Day and End Sprint
Start Time: 1 PM EST
End Time: 8 PM EST
Attendance: All Team Members (in and out, but all present for recording)
1. Did final check of app to make sure all bugs are handled and error checking is in place
2. Loaded database with pre-existing recipes in preperation of speed demo
3. Wrote script for speed demo
4. Recorded and edited demo video
5. Submitted demo video and first sprint project implementation

**Tasks Assigned**
- No tasks to work on currently. Take a break day and relax!

### Features completed after Sprint 1:
Posting your recipes
- “Create Recipe” page -- creating a new recipe would send the recipe to other users’ Browse page
- “Edit Recipe” page __for posted recipes only__
- Delete recipe from Firebase __for posted recipes only__
- Viewing your saved recipes 
- Create tabs for both posted and saved recipes, and allow user to toggle between them
- Include Floating Action Button to allow user to add a new recipe from the click of a button
Browse feature
- Swiping allows you to save the recipe
- Swipe right triggers Toast, which confirms that you saved [X] recipe
- __No filter feature for this sprint__
Login and Sign up page 
- Link the user information (username, email, and password to Firebase)
Navigation Drawer
- Have navigation drawer that access Browse, Profile, Settings, Messages pages (settings and messages pages will be placeholders for now)
Database
- Use Firebase to store user information and recipes into back-end database
- Use Firebase Cloud Storage to store recipe images

### Wednesday, April 7: Begin Sprint 2!
Start Time: 7 PM
End Time: 7:45 PM
Attendance: All Team Members
1. Caught up and reviewed the outstanding issues from Sprint 1
2. Listed necessary features for Sprint 2
3. Did initial research for Android messaging (Firebase? Outside imported library?)

**Tasks Assigned**
- No new tasks.

### Thursday, April 8: Meeting with Max
Start Time: 8 PM EST
End Time: 9 PM EST
Attendance: All Team Members
1. Max went over the initial notes and bugs he noticed from our Sprint 1 implementation
2. Specifically, he suggested that we: make swipe gesture longer (more of a swipe to register as a swipe), error check for null object on browse, minutes should go to 59, serves picker is adding 1, need a “no image” check, need to lock orientation, add divider line between My Posted Recipes / My Saved Recipes, colons after Ingredients and Steps on a recipe, replace app name in nav menu with username [and profile pic], fix numbers for minutes (00, 01, etc), edit and view recipe actions
3. We decided to meet for a few minutes after meeting with Max to subdivide these small fixes

**Tasks Assigned**
- Ryunosuke: make swipe gesture longer, minutes going to 59 (not 60), view recipe issues
- Vasant: fix the "plus 1" issue for serves picker, add divider line, fix numbers for minutes
- Anish: add colons after ingredients and steps, edit recipes issue
- Abhi: lock orientation, replace app name in nav menu

### Friday, April 9: Progress Check
Start Time: 4 PM EST
End Time: 4:15 PM EST
Attendance: Vasant, Ryunosuke, Abhi
1. Checked in on progress for smaller errors and fixes
2. Exchanged resources on broader Sprint 2 goals, like filters and messaging

**Tasks Assigned**
- Anish: continue work on adding colons after ingredients and steps, and editing recipes issue
- Ryunosuke: minutes going to 59 (not 60), view recipe issues
- Vasant: continue to fix the "plus 1" issue for serves picker, add divider line, fix numbers for minutes
- Abhi: replace app name in nav menu

### Sunday, April 11: Progress Check
Start Time: 2:30 PM EST
End Time: 3:30 PM EST
Attendance: Anish, Vasant, Ryunosuke
1. Began work on filters, examined issues with Firestore searching an array for containing multiple values
2. Looked for ways to auto-populate a AutoCompleteTextView for filters

**Tasks Assigned**
- Anish & Abhi: work on back-end issues with filtering
- Ryunosuke: fix errors with AutoCompleteTextView on Add Recipe
- Vasant: work on UI issues on Add Recipe

### Wednesday, April 14: Progress Check
Start Time: 7:00 PM EST
End Time: 8:00 PM EST
Attendance: All Team Members
1. Met and compared work on fixing issues
2. Abhi and Anish were able to successfully get filters to link to the Firebase database, within recipes
3. Ryuno was able to complete the AutoCompleteTextView, but was having issues with the populating the ListView with multiple hashtags
4. Vasant mentioned that he was able to fix small-scale errors with Add Recipe

**Tasks Assigned**
- Anish and Ryunosuke: work on fixing the ListView bug
- Abhi: Start messaging work in the back-end
- Vasant: fix issues with UI/UX in browse (filter FAB throwing error)

### Thursday, April 15: Meeting with Max
Start Time: 8 PM EST
End Time: 8:30 PM EST
Attendance: All Team Members
1. Max asked the team if we had any issues with our coding so far
2. The team presented an issue with the hashtags failing to appear on the UI side for Add Recipe/Filters pop-up, despite appearing in the back-end
3. Thanks to Max's assistance, we were able to narrow the issue to a ListView error, and planned to work further on this new issue 

**New Tasks Assigned**
- Ryunosuke: fix ListView for Hashtag List Adapter
- Anish: help Abhi with back-end work for Messaging

### Friday, April 16: Progress Check
Start Time: 4 PM EST
End Time: 4:30 PM EST
Attendance: Anish, Vasant
1. With Ryunosuke and Abhi unable to attend, we decided to review some key issues and Anish helped Vasant with UI fixes for the Messaging List page

**New Tasks Assigned**
- No new tasks.

### Sunday, April 18: Progress Check
Start Time: 2 PM EST
End Time: 3 PM EST
Attendance: All Team Members
1. Ryunosuke was able to successfully fix the issue with the ListView, allowing us to see and delete all hashtags produced for filtering.
2. Abhi and Anish were able to successfully load messages in the back-end, with some issues with visualizing the messages and the message senders in the front-end.
3. We tried to fix the front-end issues with messaging as a group, but our group effort did not pan out

**Tasks Assigned**
- Ryunosuke, Anish, and Abhi: fix issues with messaging not appearing on the front-end
- Vasant: Message page UI, start on "Settings" UI 

### Wednesday, April 21: Progress Check
Start Time: 7 PM EST
End Time: 8 PM EST
Attendance: All Team Members
1. Ryunosuke, Anish, and Abhi were able to get messagers (not messages) to appear in the Message List screen
2. Vasant fixed some design flaws with the Message List page to make the screen more appealing
3. We met for a half hour to fix the issues with messages not appearing, but resolved to have Max take a look at it tomorrow

**Tasks Assigned**
- No new tasks.

### Thursday, April 22: Meeting with Max
Start Time: 8:00 PM EST
End Time: 8:45 PM EST
Attendance: All Team Members
1. We gave Max an overview of the issues we were dealing with
2. We were able to clarify with Max the expectations laid out for us before the end of Sprint 2 and for the final Sprint

**Tasks Assigned**
- No new tasks.

### Friday, April 23: Extra Team Day
Start Time: 5 PM EST
End Time: 9 PM EST
Attendance: All Team Members
1. Abhi figured out messaging to send messages to other users.
2. Autocomplete popup working for filtering on browse page
3. Make an other’s profile adapter
4. Fixed adding hashtags and removing 

**Tasks Assigned**
- Everyone: Take a look at the UI issues

### Saturday, April 24: Extra Team Day
Start Time: 12 PM EST
End Time: 2 PM EST
Attendance: All Team Members (Ryuno had to leave a few minutes early)
1. Fix messaging UI 
2. Add users to “messaging fragment” when the user initiates a message 

**Tasks Assigned**
- Anish: Post on Piazza to get feedback from Max

### Sunday, April 25: Extra Team Day
Start Time: 1 PM EST
End Time: 7 PM EST
Attendance: All Team Members
1. Met for extra day before submission deadline to prerecord parts of the demo and fix some small issues Max pointed out
2. We got the UI working to show the hashtags on the view recipe page
3. We also got the back button added as well as the settings page done with the signout button.
4. Fixed the message button to have the correct icon

**Tasks Assigned**
No task assigned!

### Features completed after Sprint 2:
-- Fix Max’s notes
-- Other UI fixes
-- Refresh page after deleting recipe or unsaving
-- Browse
-- Other recipes should have clickable names
-- Need other users’ pages
-- They need to have a button that opens a new message __or__ existing message page if you have already messaged them (floating action button)
-- Only posted recipes with only the ability to view (not edit or delete)
-- Implement Messages
-- List of users that you have already started a convo with
-- Should have message screen with conversation abilities -> texts are stored in Firebase Cloud/Realtime
-- Filters
-- Search filters
-- Apply filters to browse
-- Add filters as a field for each recipe -> list
-- Implement Settings
-- Privacy Policy
-- Logout Button
-- Every page on the app is reversible (back buttons)
