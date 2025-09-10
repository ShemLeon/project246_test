package com.leoevg.project246.data

import android.content.Context
import android.provider.MediaStore

fun getSongs(context: Context): List<Song> {
    val songs = mutableListOf<Song>()                       // Создает пустой, изменяемый список. Этот список будет заполняться найденными объектами Song
    val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI   // Указывает место для поиска данных. Этот URI указывает на таблицу в MediaStore Android
    var selection = "${MediaStore.Audio.Media.IS_MUSIC}!=0" // Фильтр для запроса. Он говорит системе возвращать только те аудиофайлы, которые помечены как настоящая музыка (а не, например, рингтоны или звуки уведомлений).
    var sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"   // Список отсортирован по названию в алфавитном порядке

    val projection = arrayOf( // Вместо того чтобы получать все возможные данные для каждого файла, конкретно запрашиваем
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.ALBUM_ID
    )

    val cursor = context.contentResolver.query(  // фактически выполняет поиск.
        uri, projection, selection, null, sortOrder
    )

    cursor?.use {
        val idCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
        val titleCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
        val artistCol= it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
        val dataCol= it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        val albumCol= it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
        // Этот цикл проходит по каждой строке в результатах. moveToNext() перемещает курсор к следующей песне в списке и возвращает true в случае успеха.
        while (it.moveToNext()){
            val id = it.getLong( idCol)
            val title = it.getString(titleCol)
            val artist = it.getString(artistCol)
            val data = it.getString(dataCol)
            val albumId = it.getLong(albumCol)
            songs.add(Song(id, title, artist, data, albumId)) //  Создается новый объект Song с полученными данными и добавляется в список songs.
        }
    }
    return songs
}

/*
Этот файл содержит вспомогательную функцию getSongs, которая отвечает за поиск всех музыкальных файлов
на устройстве пользователя и их возврат в виде списка объектов Song. Она делает это путем запроса к MediaStore Android.
 */