// =============================================================================
## for loop
<div v-for="job in jobs" :key="job.id">{{ job.title }}</div>



// =============================================================================
## if / show 차이
https://youtu.be/F7PLPJqVotk?t=1721

v-if ==> 실제 DOM 을 넣었다(append), 뺐다(remove)
v-show ==> DOM 은 존재하지만 style 이 보이거나(display:show), 보이지 않거나(display:none)

v-if 가 v-show 보다 리소스를 더 많이 사용한다



// =============================================================================
## event - v-on
v-on@click ==> @click
@click.right="toggleModal" ==> 우측클릭일때
@click.shift="toggleModal" ==> shift키를 누르고 클릭했을때
@click.alt="toggleModal" ==> alt키를 누르고 클릭했을때
@click.self="toggleModal" ==> 해당 dom만 적용(child dom 적용되지 않음)

@click.prevent="toggleModal" ==> e.preventDefault

<form @submit="handleSubmit">
<form @submit.prevent="handleSubmit">



// =============================================================================
## handleEvent
@mouseover="handleEvent($event, 두번째인자)"



// =============================================================================
## data binding to attribute
v-bind:href ==> :href

<div class="modal" :class="{ sale: theme === 'sale' }">



// =============================================================================
## style
<style> ==> 전체 웹페이지에 적용된다
<style scoped> ==> 해당 컴포넌트에만 적용된다



// =============================================================================
## props
this.$emit('close') ==> <Modal @close="toggleModal" />



// =============================================================================
## slot
<slot></slot> ==> React 의 props.children 에 해당

<template v-slot:links> ==> <slot name="links"></slot>

<slot>default slot</slot> ==> 넘겨주는 children 이 없을때 출력된다



// =============================================================================
## teleport
<teleport to="#modals"> ==> 이 부분은 <div id="modals"> 의 하위 DOM으로 append 된다



// =============================================================================
## hooks
beforeMount()
mounted()
updated()
destroyed()



// =============================================================================
## 2-way binding : v-model
양방향 업데이트

<input type="email" v-model="email" />
<input type="checkbox" v-model="names" />
<input type="checkbox" v-model="names" />

data() {
  return {
    email: '',
    names: [],
  }
}



// =============================================================================
## router-view

<router-link to="/">HOME</router-link>

<router-view /> ==> 라우터에 의해 표현되는 뷰 영역



// =============================================================================
## router

const routes = [
  {
    path: '/jobs/:id',
    name: 'JobDetail',
    component: JobDetail,
  }
];

{{ $route.param.id }}


const routes = [
  {
    path: '/jobs/:id',
    name: 'JobDetail',
    component: JobDetail,
    props: true,
  }
];

{{ id }}

export default {
  props: ['id'],
}



// =============================================================================
## router - redirect

const routes = [
  {
    path: '/jobs',
    name: 'Job',
    component: Job,
  },
  {
    path: '/all-jobs',
    redirect: '/jobs',
  }
];



// =============================================================================
## router - history

this.$router.go(-1) ==> go back
this.$router.go(1) ==> go forward
this.$router.push({ name: 'Home' })



// =============================================================================
## fetch

mounted() {
  fetch('http://localhost:3000/jobs')
    .then(res => res.json())
    .then(data => this.jobs = data)
    .catch(err => console.log(err.message))
}




// =============================================================================
// ref

import { ref } from 'vue'

ref 사용 첫번째 경우 --> DOM 객체에 접근하기 위한 방법으로의 ref

const box = ref(null)
<p ref="box">TEXT</p>
box.value ==> 이 경우는 DOM 객체가 된다


ref 사용 두번째 경우 --> reactive 변수를 생성하는 ref

const name = ref('mario')

console.log(name.value)

setup() 안에서는 name.value 형식으로 쓰지만(name 이 ref 객체이므로)
<template> 태그 안에서는 {{ name }} 으로 쓰면 된다




// =============================================================================
// composition API

https://v3.ko.vuejs.org/ko-kr/guide/composition-api-introduction.html

동일한 논리적 관심사와 관련있는 코드를 함께 배치할 수 있다면 더 좋을 것입니다. 이것이 바로 Composition API가 할 수 있는 일입니다

컴포지션 api를 사용함으로 setup 함수에 데이터가 그룹핑 되어 보다 용이하게 데이터의 흐름을 파악하고 유지보수가 용이해집니다.

또한 함수를 재사용하기가 용이합니다. 반복되는 코드 (필터링 등등)를 import하여 컴포지션 api 내부에서 사용함으로 유틸함수 재사용에 용이합니다



import { ref, onMounted, watch, watchEffect } from 'vue'

setup(props) {
  
  const box = ref(null)
  box.value
  box.value ==> reactRef.current
  
  onMounted ==> mounted 와 동일 / setup 안에서 사용

  watch(counter, (newValue, oldValue) => {
    console.log('새로운 counter 값: ' + counter.value)
  })
  ==> counter 변수에 대한 React.useEffect 에 해당

  const stopWatch = watch(counter, (newValue, oldValue) => {
    console.log('새로운 counter 값: ' + counter.value)
  }, { deep: true })
  ==> deep:true ==> counter 가 객체(배열 포함)일 경우 내부의 값이 변하는 것까지 감지
  
  const stopEffect = watchEffect(() => {
    console.log(search.value)
  })
  ==> watchEffect 블럭 안에 있는 모든 변수들의 변화를 감지하여 실행

  반환된 stopWatch, stopEffect 는 변화 감지를 중단하는 함수
  stopWatch() ==> 해당 watch 감지 중단
  stopEffect() ==> 해당 watchEffect 감지 중단

  // `toRefs`를 사용하여 props의 `user`속성에 반응성 참조를 생성
  const { user } = toRefs(props)

  // props로 받고 반응성참조가 된 user에 감시자를 세팅
  watch(user, getUserRepositories)

}

컴포넌트 옵션 속성이 존재하기 전에 setup API가 실행되므로 this로 컴포넌트를 접근할 수 없다. 대신 context 사용. (마치 기존의 functional component를 변형해서 사용하는 느낌)


반응형 데이터는 값이 변경됨에 따라 이를 감지하고 해당 값에 종속된 작업(Side Effect)이 수행됩니다.
예를 들면 위의 message 값이 변경되면, DOM에 존재하는 div의 내부 텍스트가 알아서 변경되는 경우를 의미합니다.
컴포지션 API에서는 2가지 유형(reactive, ref)의 변경 가능한 반응형 데이터를 만들 수 있습니다.


import { reactive, ref, computed } from '@vue/composition-api'

const refValue = ref(10)
refValue.value // 10

const reactiveValue = reactive({ name: 'tom' }) ==> reactive 는 객체(object)만 가능
reactivevalue.name // tom


ref 객체는 원본 값을 value라는 속성에 담아두고 변경을 감시하는 객체이며,
reactive는 원본 객체 자체에 변경을 감지하는 옵저버를 추가하여 그대로 반환한 값 입니다.


어떤 유형의 값인지 확인하기 위한 isRef,
reactive 값을 ref 값으로 변환하는 toRefs


const useMousePosition = () => {
  const pos = reactive({ x: 0, y: 0 })
  return toRef(pos)
}

const { x, y } = useMousePosition() ==> toRef를 통해서 하나의 object 가 두개로 쪼개져 나왔다

computed는 변경 불가능한 반응형 데이터를 반환


import { onMounted, onUnmounted, onUpdated } from 'vue'

setup () {
  onBeforeMount(() => {

  })
  onMounted(() => {

  })
  onUpdated(() => {

  })
  onUnmounted(() => {

  })
  onRenderTracked(() => {

  })
  onRenderTriggered(() => {

  })
}


const matchingNames = computed(() => {
  return names.value.filter((name) => name.includes(search.value))
})





