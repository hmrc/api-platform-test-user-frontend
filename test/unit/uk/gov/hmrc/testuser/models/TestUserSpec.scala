package uk.gov.hmrc.testuser.models

import play.api.libs.json.Json
import uk.gov.hmrc.play.test.UnitSpec
import uk.gov.hmrc.testuser.models.UserTypes.{INDIVIDUAL, ORGANISATION}

class TestUserSpec extends UnitSpec {

  "Mapping test individual from Json" should {

    trait TestIndividualFromJson {
      val userId = "userId1"
      val password = "password1"
      val saUtr = "sautr123"
      val nino = "nino123"
      val jsonIndividual = Json.parse(s"""
                                         |{
                                         |  "userId":"$userId",
                                         |  "password":"$password",
                                         |  "individualDetails": {
                                         |    "firstName": "Ida",
                                         |    "lastName": "Newton",
                                         |    "dateOfBirth": "1960-06-01",
                                         |    "address": {
                                         |      "line1": "45 Springfield Rise",
                                         |      "line2": "Glasgow",
                                         |      "postcode": "TS1 1PA"
                                         |    }
                                         |  },
                                         |  "saUtr":"$saUtr",
                                         |  "nino":"$nino",
                                         |  "vrn":"vrn"
                                         |}""".stripMargin)

      val fieldDefinitions = Seq(FieldDefinition("saUtr", "Self Assessment UTR", Seq(INDIVIDUAL, ORGANISATION)))
      val testUser = Json.fromJson[TestIndividual](jsonIndividual)(new TestIndividualJsonMapper(fieldDefinitions).testIndividualReads).get
    }

    "set userId" in new TestIndividualFromJson {
      testUser.userId shouldBe userId
    }

    "set password" in new TestIndividualFromJson {
      testUser.password shouldBe password
    }

    "map fields with matching definition to fields collection" in new TestIndividualFromJson {
      testUser.fields should contain(Field("saUtr", "Self Assessment UTR", saUtr))
    }

    "map fields without matching definition to fields collection using key as label" in new TestIndividualFromJson {
      testUser.fields should contain(Field("nino", "nino", nino))
    }

    "does not map userId as a field" in new TestIndividualFromJson {
      testUser.fields.find(f => f.key == "userId") shouldBe empty
    }

    "does not map password as a field" in new TestIndividualFromJson {
      testUser.fields.find(f => f.key == "password") shouldBe empty
    }
  }

  "Mapping test organisation from Json" should {

    trait TestOrganisationFromJson {
      val userId = "userId1"
      val password = "password1"
      val saUtr = "sautr123"
      val nino = "nino123"
      val jsonOrg = Json.parse(s"""
                                         |{
                                         |  "userId":"$userId",
                                         |  "password":"$password",
                                         |  "saUtr":"$saUtr",
                                         |  "nino":"$nino",
                                         |  "vrn":"vrn"
                                         |}""".stripMargin)

      val fieldDefinitions = Seq(FieldDefinition("saUtr", "Self Assessment UTR", Seq(INDIVIDUAL, ORGANISATION)))

      val testUser = Json.fromJson[TestOrganisation](jsonOrg)(new TestOrganisationJsonMapper(fieldDefinitions).testOrganisationReads).get
    }

    "set userId" in new TestOrganisationFromJson {
      testUser.userId shouldBe userId
    }

    "set password" in new TestOrganisationFromJson {
      testUser.password shouldBe password
    }

    "map fields with matching definition to fields collection" in new TestOrganisationFromJson {
      testUser.fields should contain(Field("saUtr", "Self Assessment UTR", saUtr))
    }

    "map fields without matching definition to fields collection using key as label" in new TestOrganisationFromJson {
      testUser.fields should contain(Field("nino", "nino", nino))
    }

    "does not map userId as a field" in new TestOrganisationFromJson {
      testUser.fields.find(f => f.key == "userId") shouldBe empty
    }

    "does not map password as a field" in new TestOrganisationFromJson {
      testUser.fields.find(f => f.key == "password") shouldBe empty
    }
  }
}
