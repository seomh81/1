import os
import openai

openai.api_key ="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" #아까 복사했던 api 주소를 " " 안에 넣습니다.

question = input("무엇을 물어볼까요?: ")

completion = openai.ChatCompletion.create(
  model="gpt-3.5-turbo",
  messages=[
    {"role": "user", "content": question}
  ]
)

print(completion.choices[0].message.content)