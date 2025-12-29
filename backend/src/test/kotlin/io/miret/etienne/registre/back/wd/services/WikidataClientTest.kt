package io.miret.etienne.registre.back.wd.services

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.util.ReflectionTestUtils
import org.wikidata.wdtk.datamodel.helpers.Datamodel
import org.wikidata.wdtk.wikibaseapi.BasicApiConnection
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher

@SpringBootTest
@WireMockTest
class WikidataClientTest {

  lateinit var client: WikidataClient

  @BeforeEach
  fun setup(
    @Autowired client: WikidataClient,
    wm: WireMockRuntimeInfo,
  ) {
    this.client = client
    val wbdf = WikibaseDataFetcher(
      BasicApiConnection(wm.httpBaseUrl),
      Datamodel.SITE_WIKIDATA
    )
    ReflectionTestUtils.setField(client, "wbdf", wbdf)
  }

  @Test
  fun test() {
    WikidataClientTest::class.java.getResourceAsStream("lotr.json")!!.use {
      stubFor(post("/").willReturn(ok().withBody(it.readBytes())))
    }

    val actual = client.fetchReference(164963)

    assertThat(actual?.id).isEqualTo("Q164963")
    assertThat(actual?.label).isEqualTo("Le Seigneur des anneaux : Les Deux Tours")
  }

}
