package com.myolwinoo.universalyoga.admin.features.common

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.myolwinoo.universalyoga.admin.R
import com.myolwinoo.universalyoga.admin.data.model.YogaImage

@Composable
fun YogaImageList(
    modifier: Modifier = Modifier,
    images: List<YogaImage>,
    enableDelete: Boolean = false,
    onRemoveImage: (String) -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        contentPadding = contentPadding,
    ) {
        yogaImageList(
            images = images,
            enableDelete = enableDelete,
            onRemoveImage = onRemoveImage
        )
    }
}

private fun LazyListScope.yogaImageList(
    images: List<YogaImage>,
    enableDelete: Boolean = false,
    onRemoveImage: (String) -> Unit = {},
) {
    itemsIndexed(
        items = images,
        key = { _, item -> item.id }
    ) {  index, item ->
        if (index != 0) {
            Spacer(Modifier.size(8.dp))
        }
        Box(
            modifier = Modifier.size(100.dp),
        ) {
            item.bitmap?.asImageBitmap()?.let { bitmap ->
                Image(
                    modifier = Modifier.fillMaxSize(),
                    bitmap = bitmap,
                    contentDescription = "image",
                    contentScale = ContentScale.Crop
                )
            } ?: kotlin.run {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray),
                )
            }
            if (enableDelete) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = "delete",
                    tint = MaterialTheme.colorScheme.onError,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .background(
                            color = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(bottomStartPercent = 50)
                        )
                        .padding(
                            start = 8.dp,
                            end = 4.dp,
                            top = 4.dp,
                            bottom = 4.dp
                        )
                        .clickable { onRemoveImage(item.id) }
                        .size(24.dp)

                )
            }
        }
    }
}