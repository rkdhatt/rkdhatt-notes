# rkdhatt-notes
SYNOPSIS:
An Expense Tracker app that allows a user to track claims made, which contain expenses. User can add/edit/delete claims and expenses, change status of their claim, and send email of a selected claim. Video found in https://github.com/rkdhatt/rkdhatt-notes.

REQUIREMENTS:
Use Android 4.2.2 and must also have the following jar files: android-support-v4.jar, android-support-v7-appcompat.jar

CURRENT BUGS:
1. User can change status of their claim, however the changes are not updated in the claim list view.
2. Expenses added are not saved once you switch out of the add expense page.
3. Can email claim info and expense info only if you stay in the same page for adding expenses, otherwise only the claims can be sent (expense list not saved when switching out of activity...).
4. When adding/editing expenses, the list changes all the items in the expense list view.