#encoding=utf-8
# 查找最大约数
def findMaxFactor(num):
    count = num / 2
    while count > 1:
        if num % count == 0:
            print 'largest factor of %d is %d'%(num,count)
            break
        count -= 1
    else:
        print '%d is prime'%num

#查找两个数的最大公约数
def findMaxFactorOfTwo(num1,num2):
    count1 = num1 / 2
    count2 = num2 / 2
    if count1 > count2:
        count = count2
    elif count1 < count2:
        count = count1
    else:
        count = count1
    while count > 1:
        if num1 % count == 0 and num2 % count == 0:
            print 'the largest factor of %d,%d is %d'%(num1,num2,count)
            break
        count -= 1
    else:
        print '%d %d don\'t have factor'%(num1,num2)

#查找两个数的最小公倍数
def findMaxGBS(num1,num2):
    num1List = []
    num2List = []
    for i in range(num2):
        num1List.append((i+1)*num1)
    for j in range(num1):
        num2List.append((j+1)*num2)
    retA = [i for i in num1List if i in num2List]
    #print num1List
    #print num2List
    if retA[0] > num1*num2:
        print 'the littlest gbs is %d'%(num1*num2)
    else:
        print 'the littlest gbs id %d'%(retA[0])

for eachnum in range(1,100):
    #findMaxFactor(eachnum)
    pass

#findMaxFactorOfTwo(20,30)
findMaxGBS(35,47)
