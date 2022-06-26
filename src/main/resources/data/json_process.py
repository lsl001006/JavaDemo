# _*_ coding=utf-8 _*_
#  @Time    :2022/4/18 13:21
#  @Author  :modige
#  @Description :处理json文件 收集实体信息
import codecs

from bs4 import BeautifulSoup
from py2neo.data import Node, Relationship
from py2neo import Graph
import json
import requests
from sqlalchemy.dialects.mysql import pymysql


class json2neo:
    def __init__(self):

        pass

    def nodes_to_mysql(self):
        with codecs.open('link_detail.json', 'r', 'utf_8_sig') as fp:
            # 将json数据读取出来，存放在字典列表中
            data = json.load(fp)
            print(data['nodes'])
            nodes = data['nodes']
            ns = []
            db = pymysql.Connect(host='localhost', user='root', password='123456', db='network_kg', )
            cursor = db.cursor()
            for item in nodes:
                n = (item['id'], item['group'], item['size'], item['name'])
                ns += [n]
                sql = 'insert into nodes values ' + str(n)
                print(sql)
                cursor.execute(sql)
                db.commit()

    def triplets_to_mysql(self):
        with codecs.open('link_detail.json', 'r', 'utf_8_sig') as fp:
            # 将json数据读取出来，存放在字典列表中
            data = json.load(fp)
            print(data['links'])
            links = data['links']
            triplets = []
            db = pymysql.Connect(host='localhost', user='root', password='123456', db='network_kg', )
            cursor = db.cursor()
            for item in links:
                t = (item['source'], item['value'], item['target'])
                triplets += [t]
                sql = 'insert into triplets values (null,' + str(t).replace("(", "")
                print(sql)
                cursor.execute(sql)
                db.commit()

            print(triplets)



    def json_to_mysql(path):
        with codecs.open('node_detail.json', 'r', 'utf_8_sig') as fp:
            # 将json数据读取出来，存放在字典列表中
            data = json.load(fp)
            properties = ['identity', 'name', 'label', '中文名称', '英文名称', '英文缩写']
            # 填充缺失属性
            entities = []
            for key in data:
                entity = data[key]
                for p in properties:
                    if p not in entity:
                        entity[p] = ''
                entities += [(str(entity['identity']), entity['name'], entity['label'], entity['中文名称'],
                              entity['英文名称'], entity['英文缩写'])]

            print(entities)
            db = pymysql.Connect(host='localhost', user='root', password='123456', db='network_kg', )
            cursor = db.cursor()
            print('数据库已连接')

            for e in entities:
                sql = 'insert into entities values ' + str(e)
                print(sql)
                cursor.execute(sql)
                db.commit()

    def json2dict(self):
        group = {
            '协议':0,
            '层次':1,
            '数据':2,
            '数据单元':3,
            '服务':4,
            '硬件':5
        }
        size = {}
        for key in group:
            size[key] = (group[key]+2)*3
        with codecs.open ('知识图谱.json','r', 'utf_8_sig') as fp:

            # 将json数据读取出来，存放在字典列表中
            data = json.load(fp)
            nodes,links = [],[]
            all = {}
            node_names = []
            model = {"starships": [], "edited": "2014-12-20T21:17:50.359000Z", "name": "Lobot", "created": "2014-12-15T13:01:57.178000Z", "url": "http://swapi.co/api/people/26/", "gender": "male", "vehicles": [], "skin_color": "light", "hair_color": "none", "height": "175", "eye_color": "blue", "mass": "79", "films": ["http://swapi.co/api/films/2/"], "species": ["http://swapi.co/api/species/1/"], "homeworld": "http://swapi.co/api/planets/6/", "birth_year": "37BBY"}
            for i in data:
                # 取出头结点，尾节点，关系

                label_start = i['p']["segments"][0]['start']['labels'][0]
                name_start =  i['p']["segments"][0]['start']['properties']['name']

                start = {'id': name_start, 'class': label_start,'group':group[label_start],'size':size[label_start]}


                if name_start not in node_names:
                    nodes.append(start)
                    node_names.append(name_start)
                start['name'] = start['id']




                label_end = i['p']["segments"][0]['end']['labels'][0]
                name_end = i['p']["segments"][0]['end']['properties']['name']
                end = {'id': name_end, 'class': label_end, 'group': group[label_end], 'size': size[label_end]}

                if name_end not in node_names:
                    nodes.append(end)
                    node_names.append(name_end)
                end['name'] = end['id']
                all[name_end] = end


                rel = i['p']["segments"][0]['relationship']['type']
                link = {'source': name_start, 'target': name_end, 'value': rel}
                if link not in links:links.append(link)
            with codecs.open('final.json','w','utf_8_sig') as fin:
                fin.write(json.dumps({'nodes': nodes, 'links': links}))
                print('写入成功')
            with codecs.open('aaa.json','w','utf_8_sig') as fin:
                fin.write(json.dumps(all))
                print('写入成功')

    def get_details(self):
        with codecs.open ('final.json','r', 'utf_8_sig') as fp:

            # 将json数据读取出来，存放在字典列表中
            data = json.load(fp)
            nodes = []
            for key in data['nodes']:
                nodes.append(key['id'])
        details = {}
        for node in nodes:
            node_dict = {}
            url = 'https://baike.baidu.com/item/' +node
            node_dict['name'] = node
            node_dict['url'] = url
            html = requests.get(url,headers ={'User-Agent':'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36'})
            soup = BeautifulSoup(html.text, "html.parser")
            names = soup.select('body > div.body-wrapper > div.content-wrapper > div > div.main-content.J-content > div.basic-info.J-basic-info.cmn-clearfix')
            ans_dict = dict()
            for name in names:
                # print(name.find_all('dt'))
                print(name)
                try:
                    dt = name.find_all('dt')
                    dd = name.find_all('dd')
                    for i in range(len(dd)):
                        node_dict[dt[i].text] = dd[i].text
                except Exception as e:
                    print(e)
            with codecs.open('aaa.json','w','utf_8_sig') as fin:
                fin.write(json.dumps(details))
                print('写入成功')
            details[node] = node_dict

        print(details)






if __name__ == '__main__':

    e = json2neo()
    e.get_details()