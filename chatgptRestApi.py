import os
import openai

openai.api_key ="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"

question = input("챗GPT 질문?: ")

completion = openai.ChatCompletion.create(
  model="gpt-3.5-turbo",
  messages=[
    {"role": "user", "content": question}
  ]
)

print(completion.choices[0].message.content)