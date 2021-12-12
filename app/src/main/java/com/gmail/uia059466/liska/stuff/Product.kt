package com.gmail.uia059466.liska.stuff

import java.math.BigDecimal

sealed class ProductItem(
    val uuid: String,
    val parentUuid: String?,
    val code: String?,
    val name: String
) {
    class ProductGroup(
        uuid: String,
        parentUuid: String?,
        code: String?,
        name: String,
    ) : ProductItem(
        uuid,
        parentUuid,
        code,
        name
    )

    class Product(
        uuid: String,
        parentUuid: String?,
        code: String?,
        name: String,
        val price: BigDecimal,
        val quantity: BigDecimal,
        val description: String?,
        val measureName: String,
        val measurePrecision: Int,
    ) : ProductItem(
        uuid,
        parentUuid,
        code,
        name
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as ProductItem

        if (uuid != other.uuid) return false

        return true
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }
}