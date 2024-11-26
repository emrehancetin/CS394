package com.example.exchangeapp_11.Model

import android.os.Parcel
import android.os.Parcelable

data class CryptoModel(
    val symbol:String="",
    val shortSymbol:String="",
    val price:Double,
    val changePercent:Double,
    val AmountNumber:Double,
    val AmountDollar:Double,
    val sellPrice1:Double,
    val sellPrice2:Double,
    val sellPrice3:Double,
    val sellPrice4:Double,
    val sellPrice5:Double,
    val sellAmount1:Double,
    val sellAmount2:Double,
    val sellAmount3:Double,
    val sellAmount4:Double,
    val sellAmount5:Double,
    val buyPrice1:Double,
    val buyPrice2:Double,
    val buyPrice3:Double,
    val buyPrice4:Double,
    val buyPrice5:Double,
    val buyAmount1:Double,
    val buyAmount2:Double,
    val buyAmount3:Double,
    val buyAmount4:Double,
    val buyAmount5:Double,
    val open:Double,
    val close:Double,
    val high:Double,
    val low:Double,
    val dailyVol:Double,
    val sumboLogo:String="",
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(symbol)
        parcel.writeString(shortSymbol)
        parcel.writeDouble(price)
        parcel.writeDouble(changePercent)
        parcel.writeDouble(AmountNumber)
        parcel.writeDouble(AmountDollar)
        parcel.writeDouble(sellPrice1)
        parcel.writeDouble(sellPrice2)
        parcel.writeDouble(sellPrice3)
        parcel.writeDouble(sellPrice4)
        parcel.writeDouble(sellPrice5)
        parcel.writeDouble(sellAmount1)
        parcel.writeDouble(sellAmount2)
        parcel.writeDouble(sellAmount3)
        parcel.writeDouble(sellAmount4)
        parcel.writeDouble(sellAmount5)
        parcel.writeDouble(buyPrice1)
        parcel.writeDouble(buyPrice2)
        parcel.writeDouble(buyPrice3)
        parcel.writeDouble(buyPrice4)
        parcel.writeDouble(buyPrice5)
        parcel.writeDouble(buyAmount1)
        parcel.writeDouble(buyAmount2)
        parcel.writeDouble(buyAmount3)
        parcel.writeDouble(buyAmount4)
        parcel.writeDouble(buyAmount5)
        parcel.writeDouble(open)
        parcel.writeDouble(close)
        parcel.writeDouble(high)
        parcel.writeDouble(low)
        parcel.writeDouble(dailyVol)
        parcel.writeString(sumboLogo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CryptoModel> {
        override fun createFromParcel(parcel: Parcel): CryptoModel {
            return CryptoModel(parcel)
        }

        override fun newArray(size: Int): Array<CryptoModel?> {
            return arrayOfNulls(size)
        }
    }
}
