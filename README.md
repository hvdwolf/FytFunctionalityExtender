# FytFunctionalityExtender
Add some extra functionalities to standard (Joying) FYT firmware

So what does it do?

It "listens" to the ACCON and ACCOFF broadcasts. These are sent when the key in the contact is turned to switch on the radio (ACCON) or the contact is turned to switch off (ACCOFF).

On ACCON it can switch on Wifi. It is a bug on the Joying PX5s that when coming out of sleep, they almost never switch on WiFi.
It can also restart the active media player.

And on ACCOFF it can do the opposite:
- switch off WiFi (but why would you do that?)
- Pause the active media player (as it will actually continue for 4 minutes before android is really switched off)
