package com.sunrisesunset

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.sunrisesunset.data.dao.SunDatabase
import com.sunrisesunset.data.dao.SunDatabaseDao
import com.sunrisesunset.data.network.entityModel.EntityCity
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

class RoomTest {
    private lateinit var sunDao: SunDatabaseDao
    private lateinit var db: SunDatabase
    private lateinit var profile: EntityCity

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, SunDatabase::class.java).build()
        sunDao = db.sunDatabaseDao
        profile = EntityCity(59.43, 24.83, "Abrakadabra", "Tallinn")
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        if (db.isOpen()) db.close()
    }

    @Test
    @Throws(Exception::class)
    fun testInsertToDatabase() {
        sunDao.clearCities()
        sunDao.insertCity(profile)

        val resultProfile = sunDao.getItemById(1)

        assertNotNull(resultProfile)
        assertTrue(resultProfile.isNotEmpty())
        assertEquals(profile.term, resultProfile.get(0).term)
    }

    @Test
    @Throws(Exception::class)
    fun testInsertToDatabaseAndCompare() {
        val term = "Tallinn"
        sunDao.insertCity(profile)

        val result = sunDao.getByQuery(term)
        assertNotNull(result)
        assertThat(term, equalTo(result?.term))
    }

    @Test
    @Throws(Exception::class)
    fun testGetId() {
        val city = sunDao.getItemId("test")
        assertNull(city)
    }
}