# RetrofitAnnotations
[![](https://jitpack.io/v/oa-azab/RetrofitAnnotations.svg)](https://jitpack.io/#oa-azab/RetrofitAnnotations)

The idea is simply to add annotation to retrofit api interface methods and retrieve the annotation later via Interceptor

the library is inspired by [retroauth](https://github.com/andretietz/retroauth) 


## Example
add your custom annotation
``` kotlin
interface Service {
    @CustomAnnotation("data")
    @GET("/some/path")
    Call<ResultObject> someRequest()
}
```

get your retrofit build from `RetrofitAnnotations.builder`
it takes two parameters

 - **store** -> instance of `Store` where your annotations will be saved, you can you default implementation of it `DefaultStore`
 - **annotationClass** -> your annotation class

```kotlin
val retrofit = RetrofitAnnotations.builder(store, CustomAnnotation::class.java)
		.baseUrl("baseUrl")
		.addConverterFactory(GsonConverterFactory.create(gson))
		// add whatever you used to do with retrofit2
		// i.e.:
		.build()
```
later you can retrieve via interceptor

```kotlin
class Interceptor(private val store: DefaultStore<CustomAnnotation>) : Interceptor {  
  
    override fun intercept(chain: Interceptor.Chain): Response {  
        // get id of your request
        val requestId = store.getRequestId(chain.request())  
        
        // get your annotation from store
        val customAnnotation = store.get(requestId)  
        customAnnotation?.let {  
	        // use it
        }  
		return chain.proceed(chain.request())  
    }  
  
}
```

## Download
**Step 1.**  Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:

```groovy
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
**Step 2.**  Add the dependency

```groovy
	dependencies {
	        implementation 'com.github.oa-azab:RetrofitAnnotations:${lastest_version}'
	}
```

## LICENSE
```
Copyrights 2020 Omar Ahmed

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

<http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```
