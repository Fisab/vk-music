from flask import Flask, jsonify, request

import vk_api
from vk_api.audio import VkAudio

import json

app = Flask('vk')

with open('data.json') as data_file:    
    data = json.load(data_file)

def two_factor():
	code = input('Code? ')
	return code


@app.route('/getMusic', methods=['GET'])
def get_music():
	login, password = data['vk']['login'], data['vk']['pass']
	vk_session = vk_api.VkApi(
		login, password
	)

	try:
		vk_session.auth()
	except vk_api.AuthError as error_msg:
		print(error_msg)
		return

	vkaudio = VkAudio(vk_session)

	offset = 0
	tracks = []

	while True:
		audios = vkaudio.get(owner_id=66475832, offset=offset)
		if not audios:
			break

		offset += len(audios)
		tracks.extend(audios)

	return jsonify(tracks)


@app.errorhandler(404)
def not_found(error):
	return jsonify({'error': 'Invalid request'})


if __name__ == '__main__':
	app.run(debug=False, host='0.0.0.0')
