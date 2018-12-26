import sys
import os

ary = sys.argv
print(ary)

if ary[2]== 'createNetwork':
    result = 'Virtual network has been created (network_id {u\'mask\': 16, u\'isBooted\': False, u\'networkAddress\': 167772160, u\'controllerUrls\': [u\'tcp:211.73.95.36:6633\'], u\'tenantId\': 2}).'
 

if ary[2]== 'createSwitch':
    result = 'Virtual switch has been created (tenant_id 2, switch_id 00:a4:23:05:00:00:00:01)'

if ary[2]== 'createPort':
    if ary[4]=='cc:4e:24:c4:20:84:00:00':
        result = 'Virtual port has been created (tenant_id 2, switch_id 00:a4:23:05:00:00:00:01, port_id 1, tag 8)'
    else:
        result = 'Virtual port has been created (tenant_id 2, switch_id 00:a4:23:05:00:00:00:01, port_id 2, tag 8)'


if ary[2]== 'startPort':
    if ary[5] == '1':
        result = 'Port (port_id 1) has been started in virtual switch (tenant_id 2, switch_id 46200400562356225)'
    else:
        result = 'Port (port_id 2) has been started in virtual switch (tenant_id 2, switch_id 46200400562356225)'

if ary[2] == 'startNetwork':
    result = 'Network (tenant_id 2) has been booted'

if ary[2] == 'removeNetwork':
    result = 'Network (tenant_id 2) has been removed'

if ary[2] == 'getPhysicalTopology':
    result = '{"switches": [{"dpid": "cc:4e:24:d1:12:80:00:00"}, {"dpid": "cc:4e:24:d1:19:00:00:00"}, {"dpid": "cc:4e:24:d1:0c:00:00:00"}], "links": [{"linkId": 88.0, "dst": {"port": "5", "dpid": "cc:4e:24:d1:19:00:00:00"}, "src": {"port": "5", "dpid": "cc:4e:24:d1:12:80:00:00"}}, {"linkId": 95.0, "dst": {"port": "5", "dpid": "cc:4e:24:d1:0c:00:00:00"}, "src": {"port": "4", "dpid": "cc:4e:24:d1:19:00:00:00"}}, {"linkId": 93.0, "dst": {"port": "4", "dpid": "cc:4e:24:d1:0c:00:00:00"}, "src": {"port": "3", "dpid": "cc:4e:24:d1:12:80:00:00"}}, {"linkId": 94.0, "dst": {"port": "4", "dpid": "cc:4e:24:d1:19:00:00:00"}, "src": {"port": "5", "dpid": "cc:4e:24:d1:0c:00:00:00"}}, {"linkId": 90.0, "dst": {"port": "5", "dpid": "cc:4e:24:d1:12:80:00:00"}, "src": {"port": "5", "dpid": "cc:4e:24:d1:19:00:00:00"}}, {"linkId": 92.0, "dst": {"port": "3", "dpid": "cc:4e:24:d1:12:80:00:00"}, "src": {"port": "4", "dpid": "cc:4e:24:d1:0c:00:00:00"}}]}'

print(result)
    






