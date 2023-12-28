#version 150

uniform sampler2D Sampler0;
uniform vec4 ColorModulator;
uniform float GameTime;

in vec2 texCoord0;
in vec2 texCoord3;

out vec4 fragColor;

float rgb(float rgb) {
    return rgb/256.0;
}

bool isEmpty(vec2 pos, vec2 localPos) {
    return localPos.x < 0 || localPos.y < 0 || localPos.x > 1 || localPos.y > 1 || texture(Sampler0, pos).a == 0.0;
}

float offset(float amt, int axis) {
    return amt / textureSize(Sampler0, 0)[axis];
}

bool outer(vec2 pos, vec2 localPos) {
    float iconSize = textureSize(Sampler0, 0).x * 9 / 256.0;

    return isEmpty(pos + vec2(0, offset(1, 1)), localPos + vec2(0, 1/iconSize)) ||
        isEmpty(pos + vec2(0, -offset(1, 1)), localPos + vec2(0, -1/iconSize)) ||
        isEmpty(pos + vec2(offset(1, 0), 0), localPos + vec2(1/iconSize, 0)) ||
        isEmpty(pos + vec2(-offset(1, 0), 0), localPos + vec2(-1/iconSize, 0));
}

bool isBorder(vec2 pos, vec2 localPos) {
    if (isEmpty(pos, localPos)) return false;
    if (outer(pos, localPos)) return true;

    vec4 color = texture(Sampler0, pos);

    for (int i = 0; i < 3; i++) {
        if (color[i] > rgb(20)) return false;
    }

    return true;
}

bool isDark(vec2 pos, vec2 localPos) {
    vec4 color = texture(Sampler0, pos);

    for (int i = 0; i < 3; i++) {
        if (color[i] > rgb(80)) return false;
    }

    return true;
}

void main() {
    int index = int(texCoord3.x);
    vec2 localPos = vec2(texCoord3.x - index, texCoord3.y);

    vec4 color = texture(Sampler0, texCoord0);

    if (color.a == 0.0) {
        discard;
    }

    if (!isBorder(texCoord0, localPos) && !isDark(texCoord0, localPos)) {
        color.rgb *= vec3(1.45, 1.15, 0.45); // goldify

        float sheenTime = 150; // constant: ticks between sheens
        float targetSheenTime = texCoord3.x * 2 + (texCoord3.y - 0.5) * 1.75; // constant 1: total duration of sheen/10, constant 3: slant

        float distance = ((fract(GameTime * 24000/sheenTime) * sheenTime) - targetSheenTime) / 7.5; // constant 2: how long to fade out

        if (distance < 0 && distance < 0.3) {
            distance *= -3.3;
        }

        if (distance >= 0 && distance <= 1) {
            color.rgb = mix(color.rgb, vec3(1), (1-distance) * 0.8); // constant 3: % of the new color max
        }
    }

    fragColor = color * ColorModulator;
}
