#encoding=utf-8
'''
这段代码是从mongo数据库中读取数据，然后按照一定格式填写进execl文件中
def print_up 这个是程序运行开始头
def mongolist2xlstemp这个方法中包含的有主机从机的数据
def mongolist2xls这个方法只是单纯的读取bmu的数据写入文件
'''
import pymongo
import datetime
import cPickle as p
import xlwt
def print_up():
    client=pymongo.MongoClient('192.168.1.22',40000)#获取数据库连接
    db=client.get_database("webdtu1")#数据库名称
    #uuidList=['2b94ab91fb4455328','abea2c4a0f2c58caf','cac6deebc746abe93','6ba8df81d91458913','4bf317871f5ddc41a','290011b0639f710d4','c9533ae685aae5460','eae04b13ebd5faa50','68b86c3045377adbe','d99e4fbb5b581cb65','b9961b4abfa12e50f','b9f3570182698c224','8ab65e9e6006db7fb','e8ae106cbd5ede729','fa315eae40bc9592b','0883f9e56d9350266','7b6fe85bc6e6b6989','6a5c93f2f778fc07a','09be3cc60125a2453','f9d7f8157378733a0','1a711e33ef312159b','0a5ea5d3ccddd95b2','3a27f6193698eeb44','893ecc73661e438e0','0915f465770f13bb3','d86433e57288dd1d3','9812623129d8dcebe','ea9f3b442cf308c87','6a68f8ed7b151ac9f','da58cb15c9248bc0f','f99f7dce9f342782e','0ba7f083c8f375530','7bdc0fba7e0d0e0ae','5a5684e487241fa44','c97d861a6b52b256a','fac1b4ee97c99ecdc','5b8f3091b1c8a5b4a','baa87228748db3e05','4af52c09a5234acde','9a113971e822ed787','daac6e8745e90d3f3','eb7f393f71618d070','7baf12300f7aaa384','6aec74c0c22308616','092d8a0061953e7c5','6acc620b0804cd7c0','0b90466d9832cda73','0a7af143c02390a4c','5b7b49630b6692e12','eb9a5b59b8036899a','9b2c8e68dfd5b57f8','7a1ea4eafaec94eab']
    #cjhDict={'2b94ab91fb4455328':'LA9J1LWBXGBL8A715浦江017029','abea2c4a0f2c58caf':'LA9J1LWB5GBL8A699浦江017022','cac6deebc746abe93':'LA9J1LWB3GBL8A720浦江017020','6ba8df81d91458913':'LA9J1LWB3GBL8A717浦江017016','4bf317871f5ddc41a':'LA9J1LWB3GBL8A703浦江017015','290011b0639f710d4':'LA9J1LWB6GBL8A713浦江017014','c9533ae685aae5460':'LA9J1LWB5GBL8A704浦江017013','eae04b13ebd5faa50':'LA9J1LWBXGBL8A714浦江017011','68b86c3045377adbe':'LA9J1LWB7GBL8A719浦江017010','d99e4fbb5b581cb65':'LA9J1LWB7GBL8A705浦江017009','b9961b4abfa12e50f':'LA9J1LWB4GBL8A712浦江017008','b9f3570182698c224':'LA9J1LWB5GBL8A718浦江017007','8ab65e9e6006db7fb':'LA9J1LWB4GBL8A709浦江017006','e8ae106cbd5ede729':'LA9J1LWB1GBL8A702浦江017005','fa315eae40bc9592b':'LA9J1LWB2GBL8A708浦江017004','0883f9e56d9350266':'LA9J1LWB0GBL8A707浦江017003','7b6fe85bc6e6b6989':'LA9J1LWB9GBL8A706浦江017001','6a5c93f2f778fc07a':'LA9J9LWA7GBL8A641浦江014066','09be3cc60125a2453':'LA9J9LWA5GBL8A685浦江014060','f9d7f8157378733a0':'LA9J9LWA1GBL8A635浦江014057','1a711e33ef312159b':'LA9J9LWA8GBL8A664浦江014054','0a5ea5d3ccddd95b2':'LA9J9LWAXGBL8A679浦江014053','3a27f6193698eeb44':'LA9J9LWA0GBL8A660浦江014049','893ecc73661e438e0':'LA9J9LWA1GBL8A649浦江014046','0915f465770f13bb3':'LA9J9LWA4GBL8A645浦江014044','d86433e57288dd1d3':'LA9J9LWAXGBL8A651浦江014042','9812623129d8dcebe':'LA9J9LWA5GBL8A671浦江014041','ea9f3b442cf308c87':'LA9J9LWA9GBL8A642浦江014040','6a68f8ed7b151ac9f':'LA9J9LWA6GBL8A680浦江014039','da58cb15c9248bc0f':'LA9J9LWA5GBL8A637浦江014037','f99f7dce9f342782e':'LA9J9LWA5GBL8A668浦江014036','0ba7f083c8f375530':'LA9J9LWA3GBL8A653浦江014035','7bdc0fba7e0d0e0ae':'LA9J9LWA4GBL8A676浦江014034','5a5684e487241fa44':'LA9J9LWA5GBL8A654浦江014033','c97d861a6b52b256a':'LA9J9LWA2GBL8A664浦江014032','fac1b4ee97c99ecdc':'LA9J9LWA8GBL8A647浦江014031','5b8f3091b1c8a5b4a':'LA9J9LWA7GBL8A669浦江014028','baa87228748db3e05':'LA9J9LWA3GBL8A667浦江014027','4af52c09a5234acde':'LA9J9LWA1GBL8A652浦江014022','9a113971e822ed787':'LA9J9LWA7GBL8A655浦江014021','daac6e8745e90d3f3':'LA9J9LWA5GBL8A640浦江014018','eb7f393f71618d070':'LA9J9LWA3GBL8A670浦江014016','7baf12300f7aaa384':'LA9J9LWA2GBL8A675浦江014015','6aec74c0c22308616':'LA9J9LWA6GBL8A694浦江014011','092d8a0061953e7c5':'LA9J9LWA0GBL8A691浦江014010','6acc620b0804cd7c0':'LA9J9LWA3GBL8A698浦江014009','0b90466d9832cda73':'LA9J9LWA4GBL8A693浦江014008','0a7af143c02390a4c':'LA9J9LWA9GBL8A690浦江014007','5b7b49630b6692e12':'LA9J9LWA1GBL8A697浦江014006','eb9a5b59b8036899a':'LA9J9LWA0GBL8A688浦江014004','9b2c8e68dfd5b57f8':'LA9J9LWA8GBL8A695浦江014003','7a1ea4eafaec94eab':'LA9J9LWA2GBL8A692浦江014002'}
    uuidList=['eb9a5b59b8036899a','29901d655989ab455','0a7af143c02390a4c','092d8a0061953e7c5','7a1ea4eafaec94eab','0b90466d9832cda73','6aec74c0c22308616','9b2c8e68dfd5b57f8','5919aebc9d156d30a','5b7b49630b6692e12','6acc620b0804cd7c0','abea2c4a0f2c58caf','790b29123e4dea162','a88530160b09220c4','e8ae106cbd5ede729','4bf317871f5ddc41a','c9533ae685aae5460','d99e4fbb5b581cb65','7b6fe85bc6e6b6989','0883f9e56d9350266','fa315eae40bc9592b']
    cjhDict={'eb9a5b59b8036899a':'LA9J9LWA0GBL8A688浦江014004','29901d655989ab455':'LA9J9LWA2GBL8A689浦江014001','0a7af143c02390a4c':'LA9J9LWA9GBL8A690浦江014007','092d8a0061953e7c5':'LA9J9LWA0GBL8A691浦江014010','7a1ea4eafaec94eab':'LA9J9LWA2GBL8A692浦江014002','0b90466d9832cda73':'LA9J9LWA4GBL8A693浦江014008','6aec74c0c22308616':'LA9J9LWA6GBL8A694浦江014011','9b2c8e68dfd5b57f8':'LA9J9LWA8GBL8A695浦江014003','5919aebc9d156d30a':'LA9J9LWAXGBL8A696浦江014005','5b7b49630b6692e12':'LA9J9LWA1GBL8A697浦江014006','6acc620b0804cd7c0':'LA9J9LWA3GBL8A698浦江014009','abea2c4a0f2c58caf':'LA9J1LWB5GBL8A699浦江017022','790b29123e4dea162':'LA9J1LWB8GBL8A700浦江017018','a88530160b09220c4':'LA9J1LWBXGBL8A701浦江017002','e8ae106cbd5ede729':'LA9J1LWB1GBL8A702浦江017005','4bf317871f5ddc41a':'LA9J1LWB3GBL8A703浦江017015','c9533ae685aae5460':'LA9J1LWB5GBL8A704浦江017013','d99e4fbb5b581cb65':'LA9J1LWB7GBL8A705浦江017009','7b6fe85bc6e6b6989':'LA9J1LWB9GBL8A706浦江017001','0883f9e56d9350266':'LA9J1LWB0GBL8A707浦江017003','fa315eae40bc9592b':'LA9J1LWB2GBL8A708浦江017004'}
    account=db.get_collection("dtuMongo")

    for name in uuidList:
        filename=cjhDict[name]+'.xls'
        filexls = cjhDict[name]+'.xls'
        L=[]
        for item in account.find({'uuid':name,'insertTime':{'$gt':datetime.datetime(2017,2,11),'$lt':datetime.datetime(2017,2,18)}}):
            d={}
            d['time']=item['insertTime']
            d['soc']=item['soc']
            d['soh']=item['soh']
            d['simCard']=item['simCard']
            d['bmuCount']=item['bmuCount']
            d['batteryTotalVoltage']=item['batteryTotalVoltage']
            d['batteryTotalAmp']=item['batteryTotalAmp']
            d['positiveResistance']=item['positiveResistance']
            d['negativeResistance']=item['negativeResistance']
            d['totalResistanceValue']=item['totalResistanceValue']
            d['batterRechargeCycles']=item['batterRechargeCycles']
            d['chargingStatus']=item['chargingStatus']
            d['leakElec']=item['leakElec']
            d['alarmStatus']=item['alarmStatus']
            d['data']=item['bmu']
            d['uuid']=item['uuid']
            d['totalCapacity']=item['totalCapacity']
            d['leftCapacity']=item['leftCapacity']
            d['maxSingleVoltage']=item['maxSingleVoltage']
            d['minSingleVoltage']=item['minSingleVoltage']
            d['maxBoxTemper']=item['maxBoxTemper']
            d['minBoxTemper']=item['minBoxTemper']
            d['maxSingleBoxId']=item['maxSingleBoxId']
            d['maxSingleString']=item['maxSingleString']
            d['minSingleBoxId']=item['minSingleBoxId']
            d['minSingleString']=item['minSingleString']
            d['maxTemperBoxId']=item['maxTemperBoxId']
            d['maxTemperString']=item['maxTemperString']
            d['minTemperBoxId']=item['minTemperBoxId']
            d['minTemperString']=item['minTemperString']
            L.append(d)
        mongolist2xlstemp(filexls,L)
        print cjhDict[name],'is done'
    print 'all is done'

def mongolist2xlstemp(filename,mongodata):
    if not mongodata or len(mongodata[0]) == 0:
        return False
    wbk = xlwt.Workbook()

    firstline = mongodata[0]['data'][0]['balanceAmpList']
    seconedline=mongodata[0]['data'][0]['singleVoltageList']
    head_list=[u'时间','uuid','Soc(%)','Soh(%)',u'总压',u'电流',u'绝缘模块正极阻值(kOhm)',u'绝缘模块负极阻值(kOhm)',u'系统绝缘阻值(kOhm)',u'充电次数',u'充电故障码',u'放电故障码',u'从机个数',u'总装机容量',
    u'剩余容量',u'最大单体电压',u'最小单体电压',u'最高箱体温度',u'最低箱体温度',u'最大单点电压箱号',u'最大单点电压串号',u'最低单体电压箱号',u'最低单体电压串号',u'最高温度箱号',u'最高温度串号',u'最低温度箱号',
    u'最低温度串号']

    sheets=len(mongodata)/10000+1#这里是设置execl文件中一个sheet页面可以保存多少数据
    for sheet_index in range(sheets):
        cur_sheet_data = mongodata[10000*sheet_index:10000*sheet_index+10000]
        sheet = wbk.add_sheet('sheet_' + str(sheet_index))#如果sheet0中无法保存所有数据则新建第二个sheet进行保存
        for i in cur_sheet_data:
            vlistlen = len(i['data'][2]['singleVoltageList'])
            alistlen = len(i['data'][2]['balanceAmpList'])
            tlistlen = len(i['data'][2]['boxTemperList'])
        head_vlist = []#电压数量列表
        head_tlist = []#从机机箱温度列表
        head_alist = []#均衡电流列表
        for j in range(vlistlen):
            v = u'电压'+str(j+1)+'('+'V'+')'
            head_vlist.append(v)
        head_list.extend(head_vlist)#组装列表头
        for k in range(tlistlen):
            t = u'从机机箱'+str(k+1)+u'温度'+'('+'C'+')'
            head_tlist.append(t)
        head_list.extend(head_tlist)#组装列表头
        for m in range(alistlen):
            a = u'均衡电流'+str(m+1)+'('+'A'+')'
            head_alist.append(a)
        head_list.extend(head_alist)#组装列表头

        for item_index,item in enumerate(head_list):
            sheet.write(0,item_index,item)

        cur_line = 1
        for tiemtick in cur_sheet_data:
            cur_time = str(tiemtick['time'])#时间
            uuid = tiemtick['uuid']#uuid
            soc =  str(tiemtick['soc'])#soc
            soh = str(tiemtick['soh'])#soh
            batteryTotalVoltage = str(tiemtick['batteryTotalVoltage'])#总压
            batteryTotalAmp = str(tiemtick['batteryTotalAmp'])#电流
            positiveResistance = str(tiemtick['positiveResistance'])#绝缘正极电阻
            negativeResistance = str(tiemtick['negativeResistance'])#绝缘负极电阻
            totalResistanceValue = str(tiemtick['totalResistanceValue'])#总电阻
            batterRechargeCycles = str(tiemtick['batterRechargeCycles'])#充电次数
            chargingStatus = str(tiemtick['chargingStatus'])#充电故障码
            leakElec = str(tiemtick['leakElec'])#放电故障码
            bmuCount = tiemtick['bmuCount']#从机个数
            totalCapacity = tiemtick['totalCapacity']#总装机容量
            leftCapacity = tiemtick['leftCapacity']#剩余容量
            maxSingleVoltage = tiemtick['maxSingleVoltage']#最大单体电压
            minSingleVoltage = tiemtick['minSingleVoltage']#最小单体电压
            maxBoxTemper = tiemtick['maxBoxTemper']#最高箱体温度
            minBoxTemper = tiemtick['minBoxTemper']#最低箱体温度
            maxSingleBoxId = tiemtick['maxSingleBoxId']#最大单体电压箱号
            maxSingleString = tiemtick['maxSingleString']#最大单体电压串号
            minSingleBoxId = tiemtick['minSingleBoxId']#最低单体电压箱号
            minSingleString = tiemtick['minSingleString']#最低单体电压串号
            maxTemperBoxId = tiemtick['maxTemperBoxId']#最高温度箱号
            maxTemperString = tiemtick['maxTemperString']#最高温度串号
            minTemperBoxId = tiemtick['minTemperBoxId']#最低温度箱号
            minTemperString = tiemtick['minTemperString']#最低温度串号
            balanceAmpList = tiemtick['data'][2]['balanceAmpList']#从机均衡电流
            singleVoltageList = tiemtick['data'][2]['singleVoltageList']#从机单点电压
            boxTemperList = tiemtick['data'][2]['boxTemperList']#从机箱体温度
            balanceAmpListlen = len(tiemtick['data'][2]['balanceAmpList'])
            singleVoltageListlen = len(tiemtick['data'][2]['singleVoltageList'])
            boxTemperListlen = len(tiemtick['data'][2]['boxTemperList'])

            sheet.write(cur_line,0,cur_time)
            sheet.write(cur_line,1,uuid)
            sheet.write(cur_line,2,soc)
            sheet.write(cur_line,3,soh)
            sheet.write(cur_line,4,batteryTotalVoltage)
            sheet.write(cur_line,5,batteryTotalAmp)
            sheet.write(cur_line,6,positiveResistance)
            sheet.write(cur_line,7,negativeResistance)
            sheet.write(cur_line,8,totalResistanceValue)
            sheet.write(cur_line,9,batterRechargeCycles)
            sheet.write(cur_line,10,chargingStatus)
            sheet.write(cur_line,11,leakElec)
            sheet.write(cur_line,12,bmuCount)
            sheet.write(cur_line,13,totalCapacity)
            sheet.write(cur_line,14,leftCapacity)
            sheet.write(cur_line,15,maxSingleVoltage)
            sheet.write(cur_line,16,minSingleVoltage)
            sheet.write(cur_line,17,maxBoxTemper)
            sheet.write(cur_line,18,minBoxTemper)
            sheet.write(cur_line,19,maxSingleBoxId)
            sheet.write(cur_line,20,maxSingleString)
            sheet.write(cur_line,21,minSingleBoxId)
            sheet.write(cur_line,22,minSingleString)
            sheet.write(cur_line,23,maxTemperBoxId)
            sheet.write(cur_line,24,maxTemperString)
            sheet.write(cur_line,25,minTemperBoxId)
            sheet.write(cur_line,26,minTemperString)
            for vol in range(singleVoltageListlen):
                sheet.write(cur_line,27+vol,singleVoltageList[vol])
            for boxtemper in range(boxTemperListlen):
                sheet.write(cur_line,27+singleVoltageListlen+boxtemper,boxTemperList[boxtemper])
            for balamp in range(balanceAmpListlen):
                sheet.write(cur_line,27+singleVoltageListlen+boxTemperListlen+balamp,balanceAmpList[balamp])
            cur_line+=1
    wbk.save(filename)


def mongolist2xls(filename, mongodata):
    if not mongodata or len(mongodata[0]) == 0:
        return False

    wbk = xlwt.Workbook()

    firstline = mongodata[0]['data'][0].keys()
    firstline.remove(u'bmuNumber')
    firstline.sort()
    head_list = ['Time', u'bmuNumber'] + firstline

    sheets = len(mongodata)/6000 + 1
    for sheet_index in range(sheets):
        cur_sheet_data = mongodata[ 6000*sheet_index: 6000*sheet_index+6000 ]
        sheet = wbk.add_sheet('sheet_' + str(sheet_index))

        for item_index, item in enumerate(head_list):
            sheet.write(0, item_index, item)

        cur_line = 1
        for timetick in cur_sheet_data:
            cur_time = str(timetick['time'])
            for bmu in timetick['data']:
                sheet.write(cur_line, 0, cur_time)
                for item_index, item in enumerate(head_list[1:]):
                    sheet.write(cur_line, item_index+1, str(bmu.get(item,'')))
                cur_line += 1

    wbk.save(filename)
print_up()
