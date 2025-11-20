#!/usr/bin/env bash
adb forward tcp:1445 tcp:1445
nc -zv 127.0.0.1 1445
[ -d /mnt/jlan ] || mkdir -p /mnt/jlan
#Make sure the server is started on the phone
mount -t cifs //127.0.0.1/JLAN /mnt/jlan -o username=jlansrv,password=jlan,port=1445,vers=1.0
#It is pretty cool this actually works now. Too bad the updates on the TV boxes apps cannot write now.