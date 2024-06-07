package com.example.cognitiveassessmenttest


import org.bson.Document
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.runBlocking

//mongodb+srv://mkuzniar03:<password>@cognitive-assessment-te.s8djktu.mongodb.net/?retryWrites=true&w=majority&appName=cognitive-assessment-test


object MongoClientConnection {

    suspend fun setupConnection(
        databaseName: String = "sample_restaurants",
        connectionEnvVariable: String = "MONGODB_URI"
    ): MongoDatabase? {
        val connectString = if (System.getenv(connectionEnvVariable) != null) {
            System.getenv(connectionEnvVariable)
        } else {
            "mongodb+srv://<usename>:<password>@cluster0.sq3aiau.mongodb.net/?retryWrites=true&w=majority"
        }

        val client = MongoClient.create(connectionString = connectString)
        val database = client.getDatabase(databaseName = databaseName)

        return try {
            // Send a ping to confirm a successful connection
            val command = Document("ping", BsonInt64(1))
            database.runCommand(command)
            println("Pinged your deployment. You successfully connected to MongoDB!")
            database
        } catch (me: MongoException) {
            System.err.println(me)
            null
        }
    }
}
