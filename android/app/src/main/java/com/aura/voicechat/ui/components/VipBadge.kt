package com.aura.voicechat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aura.voicechat.R
import com.aura.voicechat.domain.model.VipTier

/**
 * VipBadge Component
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * VIP badge using authentic Yari clone assets
 * VIP uses vip_im_header.png, SVIP uses svip_1.png
 */
@Composable
fun VipBadge(
    vipTier: VipTier,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 20.dp
) {
    if (vipTier == VipTier.NONE) return
    
    val drawableRes = when (vipTier) {
        VipTier.VIP -> R.drawable.vip_im_header // Purple VIP badge from Yari
        VipTier.SVIP -> R.drawable.svip_1 // Gold SVIP badge from Yari
        else -> return
    }
    
    Image(
        painter = painterResource(id = drawableRes),
        contentDescription = vipTier.displayName,
        modifier = modifier.size(size),
        contentScale = ContentScale.Fit
    )
}
