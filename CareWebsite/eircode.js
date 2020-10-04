const axios = require('axios')

class EircodeHandler {
	static initialize() {
		this.s = axios.create({
			baseURL: "https://api-finder.eircode.ie/Q317/",
			timeout: 5000,
			headers: {'User-Agent': "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:57.0) Gecko/20100101 Firefox/57.0"}
		})
	}

	static async get_key() {
		try {
			const res = await this.s.get("findergetidentity")
			return res.data.key
		} catch (error) {
			console.log(error)
			return null
		}
	}

	static async get_address(key, zipcode) {
		const params = {
			key: key,
			address: zipcode,
			language: "en",
			geographicAddress: true
		}
		try {
			const res = await this.s.get("finderfindaddress", { params: params })
			if (res.data.result.code != 100) {
				return null
			}
			return {
				txn: res.data.input.txn,
				addressId: res.data.addressId,
				postalAddress: res.data.postalAddress,
				geographicAddress: res.data.geographicAddress
			}
		} catch (error) {
			console.log(error)
			return null
		}
	}

	static async get_coordinates(zipcode) {
		const key = await this.get_key()
		if (key == null) {
			console.log('Failed to get key.')
			return null
		}
		const address = await this.get_address(key, zipcode)
		if (address == null) {
			console.log('Failed to get address.')
			return null
		}
		const params = {
			key: key,
			addressId: address.addressId,
			txn: address.txn,
			history: false,
			geographicAddress: true
		}
		try {
			const res = await this.s.get("findergetecaddata", { params: params })
			res.data.spatialInfo.etrs89.location.eircode = res.data.eircodeInfo.eircode
			res.data.spatialInfo.etrs89.location.address = res.data.geographicAddress.english
			return res.data.spatialInfo.etrs89.location
		} catch (error) {
			console.log(error)
			return null
		}
	}
}

EircodeHandler.initialize()
module.exports = EircodeHandler;