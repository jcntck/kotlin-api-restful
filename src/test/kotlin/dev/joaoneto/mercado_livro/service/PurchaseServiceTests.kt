package dev.joaoneto.mercado_livro.service

import dev.joaoneto.mercado_livro.events.PurchaseEvent
import dev.joaoneto.mercado_livro.helper.buildPurchsase
import dev.joaoneto.mercado_livro.repository.PurchaseRepository
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ApplicationEventPublisher
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class PurchaseServiceTests {
    @MockK
    private lateinit var purchaseRepository: PurchaseRepository

    @MockK
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @InjectMockKs
    private lateinit var purchaseService: PurchaseService

    val purchaseEventSlot = slot<PurchaseEvent>()

    @Before
    fun setUp() = MockKAnnotations.init(this, relaxUnitFun = true)

    @Test
    fun `should create purchase and publish event`() {
        val fakePurchase = buildPurchsase()

        every { purchaseRepository.save(fakePurchase) } returns fakePurchase
        every { applicationEventPublisher.publishEvent(any()) } just runs

        purchaseService.create(fakePurchase)

        verify(exactly = 1) { purchaseRepository.save(fakePurchase) }
        verify(exactly = 1) { applicationEventPublisher.publishEvent(capture(purchaseEventSlot)) }

        assertEquals(fakePurchase, purchaseEventSlot.captured.purchaseModel)
    }

    @Test
    fun `should update purchase`() {
        val purchase = buildPurchsase()

        every { purchaseRepository.save(purchase) } returns purchase

        purchaseService.update(purchase)

        verify(exactly = 1) { purchaseRepository.save(purchase) }
    }
}