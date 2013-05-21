import map_reduce
import random
import json

def paretosample(n,power=2.0):
  m = n+1
  while m > n: m = numpy.random.zipf(power)
  return m

def initialize():
    json_data = open('test.json')
    data = json.load(json_data)
    id = 0
    aux = {}
    aux2 = {}
    for key in data:
        aux2[id] = key
        aux[key] = id
        id += 1
    dictdata = {}
    id = 0
    p = 1.0/len(aux)
    links = []
    for key in data:
        links = []
        for link in data[key]:
            links.append(aux[link])
        dictdata[id] = [p,len(links),links]
        id += 1
    json_data.close()
    return [dictdata,aux,aux2]

def ip_mapper(input_key,input_value):
  if input_value[1] == 0: return [(1,input_value[0])]
  else: return []

def ip_reducer(input_key,input_value_list):
  return sum(input_value_list)

def pr_mapper(input_key,input_value):
  return [(input_key,0.0)]+[(outlink,input_value[0]/input_value[1])
          for outlink in input_value[2]]

def pr_reducer_inter(intermediate_key,intermediate_value_list,
                     s,ip,n):
  return (intermediate_key,
          s*sum(intermediate_value_list)+s*ip/n+(1.0-s)/n)

def pagerank(i,s=0.85,tolerance=0.00001):
  n = len(i)
  iteration = 1
  change = 2
  while change > tolerance:
    print "Iteration: "+str(iteration)
    ip_list = map_reduce.map_reduce(i,ip_mapper,ip_reducer)
    if ip_list == []: ip = 0
    else: ip = ip_list[0]
    pr_reducer = lambda x,y: pr_reducer_inter(x,y,s,ip,n)
    new_i = map_reduce.map_reduce(i,pr_mapper,pr_reducer)
    change = sum([abs(new_i[j][1]-i[j][0]) for j in xrange(n)])
    print "Change in l1 norm: "+str(change)
    for j in xrange(n): i[j][0] = new_i[j][1]
    iteration += 1
  return i

def ranks(new_i):
    ranks = []
    nlinks = 0
    for page in new_i:
        ranks.append(new_i[page][0])
        nlinks += new_i[page][1]
    return [ranks,nlinks]

[i,aux,aux2] = initialize()
new_i = pagerank(i,0.85,0.0001)
[rank,nlinks] = ranks(new_i)
print "La suma de todos los page ranke:" +str(sum(rank))
print "El mayor page rank:"+str(max(rank))+str(aux2[rank.index(max(rank))])
print "El menor page rank:"+str(min(rank))+str(aux2[rank.index(min(rank))])
print "Número de links existentes:" +str(nlinks)
print "Número de nodos:" +str(len(new_i))
print "El top 10:"
for i in range(10):
    print str(i) + " " +"pager: "+str(max(rank)) +"asignado a: "+str(aux2[rank.index(max(rank))])
    rank.pop(rank.index(max(rank)))    
print(new_i)

