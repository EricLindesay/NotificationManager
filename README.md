# NotificationManager
Notification Manager for Android using Kotlin



## Permissions Required
Special Permissions
- Do Not Disturb access
- Device and app noticiations

Don't need full-screen notifications, these are things like how phone calls and alarms take up your entire screen

## Features
- Track how many notifications from each app
- have a histogram of time like the phone does for app use
- Have a notification history
    - probs want to limit to a certain number of days/let user choose
- Change sound of notifications per notification
- Be able to block certain notifications from certain apps or which contain certain key words or something.
- Be able to rearrange the notification layout (depends on how much info notification permissions gives you).
- Have different settings e.g. certain notifications are for "Work Mode" and you can set when work mode is for. You can also manually enter these modes
    - Manually entering the modes can be done in the app
    - Or in a home-screen widget
    - Or in the Quick Settings menu.
    - You could cycle through them on the button like battery saver.
    - But the modes probs arent mutually exclusive
- Power use for notifications
- User can specify notification levels
    - One app can send notifications in multiple ways, you have to manage this
    - Regular notifications are as usual
    - Silent notifications show a silent notification from the app saying you have X silent notifications
    - Blocked notifications aren't shown to the user but they can be found in the app
- Sort notifications by status, post time etc

- Create rules
    - A rule can have a start and end time and day of the week and for a specific (or multiple) apps
    - An action associated with it (dismiss, mute, snooze, change the notif sound, replace, read out, change notification so it is like a call, auto tap button e.g. auto reply, open directly)
    - Rules should be able to be very complex, there should be a button to extend. When you edit a rule it will look something like this
        - Rule: IF (app is WhatsAPP +modifier(and/or)) + and/or
            - Which then goes to IF (app is Whatsapp AND ...)
    - Apps should be picked from a picker menu which lists all the apps, the location (com.azure.authenticator) and a checkbox of "include in rule". Also note whether its a system app
    - Rules should also be able to only be active in certain states e.g. if the screen is on or off, if it is charging wired, wirelessly or not.
    - If contains, matches, match regex, match json
    - AI if similar to (e.g. if similar to "me too")
    - import/export rules
    - Rules can also set the notification's priority.
    - Spam blocking..?

- You can also create rules about phone calls this is a bit away from notification manager so maybe best to leave
    - auto hang up
    - auto ignore

- You can run all stored messages on the rule to see if it catches what you want.


