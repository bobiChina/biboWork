#! /usr/bin/env python
#encoding=utf-8
import os
import time
import datetime
#datetime.date.today().weekday()  星期一0  星期天6
if datetime.date.today().weekday()<5:
    timeLimit = 20
else:
    timeLimit = 40
f = open('F:\\thefile.txt','r+')
f_date = f.readline()
f.close

n_date = time.strftime('%Y-%m-%d')+'\n'

if f_date != n_date:
    f = open('F:\\thefile.txt','r+')
    f.truncate()
    f.close()
    f = open('F:\\thefile.txt','w')
    f.write((n_date))
    run_time = '0'
    f.write(run_time)
    f.close

while 2>1:
    f = open('F:\\thefile.txt','r+')
    f_date = f.readline()
    run_time = f.readline()
    run = int(run_time)

    time.sleep(1)#1是秒
    if run<timeLimit:
        run = run + 1
        f.truncate()
        f.close
        f = open('F:\\thefile.txt','w')
        f.write(f_date)
        run_time = str(run)
        f.write(run_time)
        f.close
    else:
        break
cmd = 'cmd.exe /k shutdown -s -t 0'
os.system(cmd)
