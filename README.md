# FytFunctionalityExtender
Add some extra functionalities to standard (Joying) FYT firmware. This FYT firmware is used on Android car head units wich are based on FYT MCU and hardware and the software on top of that.

# This does work on android 6 but not (yet?) on android 8 due to the background limitations.

![logo](https://github.com/hvdwolf/FytFunctionalityExtender/blob/master/logo.png)

So what does it do?

It "listens" to the ACCON and ACCOFF broadcasts. These are sent when the key in the contact is turned to switch on the head unit (ACCON), or when the contact is turned to switch the head unit off (ACCOFF).

On ACCON:
- it can switch on Wifi. It is a bug on the Joying PX5s that when coming out of sleep, they almost never switch on WiFi.
- It can also restart the active media player using "input keyevent 126".

And on ACCOFF it can do the opposite:
- switch off WiFi (but why would you do that?)
- Pause the active media player (as it will actually continue for 4 minutes before android is really switched off), using "input keyevent 127".
