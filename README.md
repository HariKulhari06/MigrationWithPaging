# MigrationWithPaging

Sample created to practice **Paging Library**.

## What Have I learned?

* **Paging Library:** The Paging Library makes it easier for you to load data on demand within your app's RecyclerView.

* **PagedList:** Collection that loads data in pages, asynchronously. A PagedList can be used to load data from sources you define, and present it easily in your UI with a RecyclerView.

* **DataSource and DataSource.Factory:** DataSource is the base class for loading snapshots of data into a PagedList. A DataSource.Factory is used to create a DataSource.

* **PageKeyedDataSource:** DataSource used to load embed next/previous keys.

* **ItemKeyedDataSource:** DataSource used to retrieve data from item N to fetch item N+1

* **PositionalDataSource:** if you need to fetch pages of data from any location you choose.

* **LivePagedListBuilder:** Class used to build a LiveData<PagedList> based on DataSource.Factory and a PagedList.Config.

* **RxPagedListBuilder:** Class used to build a Observable<PagedList> based on DataSource.Factory and a PagedList.Config.

* **BoundaryCallback:** Helper callback to signals when a PagedList has reached the end of available data.

* **PagedListAdapter:** RecyclerView.Adapter that presents paged data from PagedLists in a RecyclerView. PagedListAdapter listens to PagedList loading callbacks as pages are loaded, and uses DiffUtil to compute fine grained updates as new PagedLists are received.

### Resources to start with Paging Library on Android

* [Paging library overview Android by Google][10]
* [Paging Library CodeLab][11]
* [PagingWithNetworkSample by Yigit Boyar][12]
* [Android Jetpack: manage infinite lists with RecyclerView and Paging (Google I/O '18)][13]
* [Android Jetpack: what's new in Architecture Components (Google I/O '18)][13]

 [9]: https://developer.android.com/topic/libraries/architecture/paging/
[10]: https://codelabs.developers.google.com/codelabs/android-paging/index.html?index=..%2F..%2Findex#0
[11]: https://github.com/googlesamples/android-architecture-components/tree/master/PagingWithNetworkSample
[12]: https://www.youtube.com/watch?v=BE5bsyGGLf4
[13]: https://www.youtube.com/watch?v=pErTyQpA390&t=862s
[14]: https://antonioleiva.com/kotlin-android-developers-book/



Do you want to contribute?
--------------------------

Feel free to report or add any useful feature, I will be glad to improve it with your help.

Developed By
------------

* Erik Jhordan Rey  - <hari.kulhari@ranosys.com> 

License
-------

    Copyright 2019 Hari Singh Kulhari

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
