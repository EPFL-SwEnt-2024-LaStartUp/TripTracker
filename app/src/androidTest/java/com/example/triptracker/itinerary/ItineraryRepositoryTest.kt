package com.example.triptracker.itinerary

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.model.itinerary.Itinerary
import com.example.triptracker.model.location.Location
import com.example.triptracker.model.location.Pin
import com.example.triptracker.model.repository.ItineraryRepository
import com.example.triptracker.viewmodel.IncrementableField
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.play.core.assetpacks.db
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.*

import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import net.bytebuddy.implementation.InvokeDynamic.lambda
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ItineraryRepositoryTest {


    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var mockFirestore: FirebaseFirestore

    @RelaxedMockK
    private lateinit var mockCollectionReference: CollectionReference

    @RelaxedMockK
    private lateinit var mockDocumentReference: DocumentReference

    @RelaxedMockK
    private lateinit var mockQuerySnapshot: QuerySnapshot

    @RelaxedMockK
    private lateinit var mockDocumentSnapshot: DocumentSnapshot

    private lateinit var itineraryRepository: ItineraryRepository

    @RelaxedMockK
    private lateinit var mockDb: FirebaseFirestore

    @RelaxedMockK
    private lateinit var mockTask: Task<Void>

    @RelaxedMockK
    private lateinit var mockTransaction: Transaction

    @RelaxedMockK
    private lateinit var mockVoidTask: Task<Void>


    @RelaxedMockK
    private lateinit var mockDocumentSnapshotTask: Task<DocumentSnapshot>

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        itineraryRepository = spyk(ItineraryRepository(), recordPrivateCalls = true)

        // Mocking Firebase Firestore
        every { mockDb.collection(any()).document(any()) } returns mockDocumentReference
        every { mockDocumentReference.get() } returns mockDocumentSnapshotTask
        every { mockTransaction.get(mockDocumentReference) } returns mockDocumentSnapshot
        every { mockDb.runTransaction(any<Transaction.Function<Void>>()) } returns mockVoidTask

        // Replacing the db instance in itineraryRepository with the mock
        itineraryRepository.db = mockDb
    }

    @Test
    fun testGetAllItineraries() {
        // Arrange
        val mockTask = Tasks.forResult(mockQuerySnapshot)
        every { mockCollectionReference.get() } returns mockTask
        every { mockQuerySnapshot.documents } returns emptyList()

        // Act
        itineraryRepository.getAllItineraries { itineraries ->
            // Assert
            assertTrue(itineraries.isEmpty())
        }
    }

    @Test
    fun testGetItineraryById() {
        // Arrange
        val itineraryId = "testItineraryId"
        val mockTask = Tasks.forResult(mockDocumentSnapshot)
        every { mockCollectionReference.document(itineraryId) } returns mockDocumentReference
        every { mockDocumentReference.get() } returns mockTask

        val mockLocationData = mapOf(
            "latitude" to 37.7749,
            "longitude" to -122.4194,
            "name" to "San Francisco"
        )

        every { mockDocumentSnapshot.exists() } returns true
        every { mockDocumentSnapshot.data } returns mapOf(
            "title" to "Test Itinerary",
            "userMail" to "test@example.com",
            "location" to mockLocationData,
            "flameCount" to 0L,
            "saves" to 0L,
            "clicks" to 0L,
            "numStarts" to 0L,
            "startDateAndTime" to "",
            "endDateAndTime" to "",
            "pinnedPlaces" to emptyList<Map<String, Any>>(),
            "description" to "",
            "route" to emptyList<Map<String, Any>>()
        )

        // Act
        itineraryRepository.getItineraryById(itineraryId) { itinerary ->
            // Assert
            assertNotNull(itinerary)
            assertEquals("Test Itinerary", itinerary?.title)
        }
    }

    @Test
    fun testGetPinNames() {
        // Arrange
        val pin1 = Pin(0.3, 0.45, "Place 1", "Address 1", listOf("12345"))
        val pin2 = Pin(0.4, 0.55, "Place 2", "Address 2", listOf("54321"))
        val itinerary = mockk<Itinerary> {
            every { pinnedPlaces } returns listOf(pin1, pin2)
        }

        // Act
        val pinNames = itineraryRepository.getPinNames(itinerary)

        // Assert
        assertEquals(listOf("Place 1", "Place 2"), pinNames)
    }

    @Test
    fun testUpdateItinerary() {
        // Arrange
        val itinerary = Itinerary(
            id = "itineraryId",
            title = "Trip to Paris",
            userMail = "user@example.com",
            location = Location(latitude = 48.8566, longitude = 2.3522, name = "Paris"),
            flameCount = 10,
            saves = 5,
            clicks = 20,
            numStarts = 3,
            startDateAndTime = "2024-05-01T10:00:00Z",
            endDateAndTime = "2024-05-07T18:00:00Z",
            pinnedPlaces = listOf(
                Pin(latitude = 48.8584, longitude = 2.2945, name = "Eiffel Tower", description = "A famous landmark", image_url = listOf("https://example.com/eiffel.jpg")),
                Pin(latitude = 48.8606, longitude = 2.3376, name = "Louvre Museum", description = "The world's largest art museum", image_url = listOf("https://example.com/louvre.jpg"))
            ),
            description = "A week-long trip to Paris.",
            route = listOf(LatLng(48.8584, 2.2945), LatLng(48.8606, 2.3376))
        )

        val itineraryData = hashMapOf<String, Any>(
            "id" to itinerary.id,
            "title" to itinerary.title,
            "userMail" to itinerary.userMail,
            "location" to hashMapOf(
                "latitude" to itinerary.location.latitude,
                "longitude" to itinerary.location.longitude,
                "name" to itinerary.location.name
            ),
            "flameCount" to itinerary.flameCount,
            "saves" to itinerary.saves,
            "clicks" to itinerary.clicks,
            "numStarts" to itinerary.numStarts,
            "startDateAndTime" to itinerary.startDateAndTime,
            "endDateAndTime" to itinerary.endDateAndTime,
            "pinnedPlaces" to itinerary.pinnedPlaces.map { pin ->
                hashMapOf(
                    "latitude" to pin.latitude,
                    "longitude" to pin.longitude,
                    "name" to pin.name,
                    "description" to pin.description,
                    "image-url" to pin.image_url
                )
            },
            "description" to itinerary.description,
            "route" to itinerary.route.map { latLng ->
                hashMapOf(
                    "latitude" to latLng.latitude,
                    "longitude" to latLng.longitude
                )
            }
        )

        // Mock the behavior of the Task returned by set()
        every { mockTask.addOnSuccessListener(any<OnSuccessListener<Void>>()) } answers {
            firstArg<OnSuccessListener<Void>>().onSuccess(null)
            mockTask
        }
        every { mockTask.addOnFailureListener(any<OnFailureListener>()) } answers {
            firstArg<OnFailureListener>().onFailure(Exception("Test Exception"))
            mockTask
        }

        // Act
        itineraryRepository.updateItinerary(itinerary)

        // Assert
        verify { mockDocumentReference.set(itineraryData) }
        verify { mockTask.addOnSuccessListener(any<OnSuccessListener<Void>>()) }
        verify { mockTask.addOnFailureListener(any<OnFailureListener>()) }
    }


    @Test
    fun testIncrementField() {
        // Arrange
        val itineraryId = "itineraryId"
        val field = IncrementableField.FLAME_COUNT
        val fieldString = "flameCount"
        val initialCount = 10L
        every { mockDocumentSnapshot.getLong(fieldString) } returns initialCount

        // Mock the behavior of the Task returned by runTransaction
        every { mockVoidTask.addOnSuccessListener(any<OnSuccessListener<Void>>()) } answers {
            firstArg<OnSuccessListener<Void>>().onSuccess(null)
            mockVoidTask
        }
        every { mockVoidTask.addOnFailureListener(any<OnFailureListener>()) } answers {
            firstArg<OnFailureListener>().onFailure(Exception("Test Exception"))
            mockVoidTask
        }

        // Mocking the runTransaction method to properly handle the transaction function
        every {
            mockDb.runTransaction(captureLambda())
        } answers {
            lambda<Transaction.Function<Void>>().captured.apply(mockTransaction)
            mockVoidTask
        }

        // Act
        itineraryRepository.incrementField(itineraryId, field)

        // Assert
        verify { mockTransaction.get(mockDocumentReference) }
        verify { mockTransaction.update(mockDocumentReference, fieldString, initialCount + 1) }
        verify { mockVoidTask.addOnSuccessListener(any<OnSuccessListener<Void>>()) }
        verify { mockVoidTask.addOnFailureListener(any<OnFailureListener>()) }
    }
}