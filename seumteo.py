import requests

url = 'http://apis.data.go.kr/1613000/ArchPmsService_v2/getApPlatPlcInfo'
#params ={'serviceKey' : '6kuu7kEFGKJAGzOR9ig8UVglU1o/AdPdJ/XdolWQ8szEQdBFOUh6VDdsWALPQCaGAjnWJqDl+C4Mw+Gf2GPx5w==', 'sigunguCd' : '11680', 'bjdongCd' : '10300', 'platGbCd' : '0', 'bun' : '0012', 'ji' : '0004', 'startDate' : '', 'endDate' : '', 'numOfRows' : '10', 'pageNo' : '1' }
params ={'serviceKey' : '6kuu7kEFGKJAGzOR9ig8UVglU1o/AdPdJ/XdolWQ8szEQdBFOUh6VDdsWALPQCaGAjnWJqDl+C4Mw+Gf2GPx5w==', 'sigunguCd' : '11680', 'bjdongCd' : '', 'platGbCd' : '0', 'bun' : '', 'ji' : '', 'startDate' : '', 'endDate' : '', 'numOfRows' : '100', 'pageNo' : '1' }

response = requests.get(url, params=params)
print(response.content)