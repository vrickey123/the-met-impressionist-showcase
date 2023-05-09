package com.vrickey123.network.local

class FakeMetDatabase: MetDatabase {

    private val fakeMetObjectDao = FakeMetObjectDao()

    override fun metObjectDAO(): MetObjectDAO = fakeMetObjectDao

    fun setIsSuccess(isSuccess: Boolean) {
        fakeMetObjectDao.isSuccess = isSuccess
    }

    fun setIsEmpty(isEmpty: Boolean) {
        fakeMetObjectDao.isEmpty = isEmpty
    }
}