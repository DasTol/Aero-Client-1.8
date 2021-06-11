package net.aeroclient.client.font;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public final class ACFontRenderer
        extends ACFont {
    protected final ACFont.CharData[] boldItalicChars = new ACFont.CharData[256];
    protected final ACFont.CharData[] italicChars = new ACFont.CharData[256];
    protected final ACFont.CharData[] boldChars = new ACFont.CharData[256];
    private final int[] colorCode = new int[32];
    private char COLOR_CODE_START = (char)167;
    protected DynamicTexture texBold;
    protected DynamicTexture texItalic;
    protected DynamicTexture texItalicBold;

    public ACFontRenderer(ResourceLocation resourceLocation, float size) {
        super(resourceLocation, size);
        this.setupMinecraftColorCodes();
        this.setupBoldItalicIDs();
    }

    public float drawStringWithShadow(String text, double x, double y, int color, int shadowColor) {
        float shadowWidth = this.drawString(text, x + 1.0, y + 1.0, shadowColor, false);
        return Math.max(shadowWidth, this.drawString(text, x, y, color, false));
    }

    public float drawStringWithShadow(String text, double x, double y, int color) {
        float shadowWidth = this.drawString(text, x + 1.0, y + 1.0, color, true);
        return Math.max(shadowWidth, this.drawString(text, x, y, color, false));
    }

    public float drawString(String text, float x, float y, int color) {
        return this.drawString(text, x, y, color, false);
    }

    public float drawCenteredString(String text, float x, float y, int color) {
        return this.drawString(text, x - (float)(this.getStringWidth(text) / 2), y, color);
    }

    public float drawCenteredStringWithShadow(String text, float x, float y, int color) {
        this.drawString(text, (double)(x - (float)(this.getStringWidth(text) / 2)) + 1.0, (double)y + 1.0, color, true);
        return this.drawString(text, x - (float)(this.getStringWidth(text) / 2), y, color);
    }

    public float drawString(String text, double x, double y, int color, boolean shadow) {
        x -= 1.0;
        y -= 0.5;
        if (text == null) {
            return 0.0f;
        }
        if (color == 0x20FFFFFF) {
            color = 0xFFFFFF;
        }
        if ((color & 0xFC000000) == 0) {
            color |= 0xFF000000;
        }
        if (shadow) {
            color = (color & 0xFCFCFC) >> 2 | color & 0xFF00FF;
        }
        ACFont.CharData[] currentData = this.charData;
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        boolean bold = false;
        boolean italic = false;
        boolean strike = false;
        boolean underline = false;
        boolean render = true;
        x *= 2.0;
        y = (y - 5.0) * 2.0;
        if (render) {
            GL11.glPushMatrix();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glScaled(0.5, 0.5, 0.5);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f((float)(color >> 16 & 0xFF) / 255.0f, (float)(color >> 8 & 0xFF) / 255.0f, (float)(color & 0xFF) / 255.0f, alpha);
            int size = text.length();
            GL11.glEnable(3553);
            GL11.glBindTexture(3553, this.tex.getGlTextureId());
            for (int i = 0; i < size; ++i) {
                char character = text.charAt(i);
                if (character == this.COLOR_CODE_START && i < size) {
                    int colorIndex = 21;
                    try {
                        colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (colorIndex < 16) {
                        bold = false;
                        italic = false;
                        underline = false;
                        strike = false;
                        GL11.glBindTexture(3553, this.tex.getGlTextureId());
                        currentData = this.charData;
                        if (colorIndex < 0 || colorIndex > 15) {
                            colorIndex = 15;
                        }
                        if (shadow) {
                            colorIndex += 16;
                        }
                        int cc = this.colorCode[colorIndex];
                        GL11.glColor4f((float)(cc >> 16 & 0xFF) / 255.0f, (float)(cc >> 8 & 0xFF) / 255.0f, (float)(cc & 0xFF) / 255.0f, alpha);
                    } else if (colorIndex != 16) {
                        if (colorIndex == 17) {
                            bold = true;
                            if (italic) {
                                GL11.glBindTexture(3553, this.texItalicBold.getGlTextureId());
                                currentData = this.boldItalicChars;
                            } else {
                                GL11.glBindTexture(3553, this.texBold.getGlTextureId());
                                currentData = this.boldChars;
                            }
                        } else if (colorIndex == 18) {
                            strike = true;
                        } else if (colorIndex == 19) {
                            underline = true;
                        } else if (colorIndex == 20) {
                            italic = true;
                            if (bold) {
                                GL11.glBindTexture(3553, this.texItalicBold.getGlTextureId());
                                currentData = this.boldItalicChars;
                            } else {
                                GL11.glBindTexture(3553, this.texItalic.getGlTextureId());
                                currentData = this.italicChars;
                            }
                        } else if (colorIndex == 21) {
                            bold = false;
                            italic = false;
                            underline = false;
                            strike = false;
                            GL11.glColor4f((float)(color >> 16 & 0xFF) / 255.0f, (float)(color >> 8 & 0xFF) / 255.0f, (float)(color & 0xFF) / 255.0f, alpha);
                            GL11.glBindTexture(3553, this.tex.getGlTextureId());
                            currentData = this.charData;
                        }
                    }
                    ++i;
                    continue;
                }
                if (character >= currentData.length) continue;
                GL11.glBegin(4);
                this.drawChar(currentData, character, (float)x, (float)y + 6.0f);
                GL11.glEnd();
                if (strike) {
                    this.drawLine(x, y + (double)(currentData[character].height / 2), x + (double)currentData[character].width - 8.0, y + (double)(currentData[character].height / 2), 1.0f);
                }
                if (underline) {
                    this.drawLine(x, y + (double)currentData[character].height - 2.0, x + (double)currentData[character].width - 8.0, y + (double)currentData[character].height - 2.0, 1.0f);
                }
                x += (double)(currentData[character].width - 8 + this.charOffset);
            }
            GL11.glHint(3155, 4352);
            GL11.glPopMatrix();
        }
        return (float)x / 2.0f;
    }

    @Override
    public int getStringWidth(String text) {
        if (text == null) {
            return 0;
        }
        int width = 0;
        ACFont.CharData[] currentData = this.charData;
        boolean bold = false;
        boolean italic = false;
        int size = text.length();
        for (int i = 0; i < size; ++i) {
            char character = text.charAt(i);
            if (character == this.COLOR_CODE_START && i < size) {
                int colorIndex = "0123456789abcdefklmnor".indexOf(character);
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                } else if (colorIndex == 17) {
                    bold = true;
                    currentData = italic ? this.boldItalicChars : this.boldChars;
                } else if (colorIndex == 20) {
                    italic = true;
                    currentData = bold ? this.boldItalicChars : this.italicChars;
                } else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    currentData = this.charData;
                }
                ++i;
                continue;
            }
            if (character >= currentData.length) continue;
            width += currentData[character].width - 8 + this.charOffset;
        }
        return width / 2;
    }

    @Override
    public void setAntiAlias(boolean antiAlias) {
        super.setAntiAlias(antiAlias);
        this.setupBoldItalicIDs();
    }

    @Override
    public void setFractionalMetrics(boolean fractionalMetrics) {
        super.setFractionalMetrics(fractionalMetrics);
        this.setupBoldItalicIDs();
    }

    private void setupBoldItalicIDs() {
        this.texBold = this.setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
        this.texItalic = this.setupTexture(this.font.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
        this.texItalicBold = this.setupTexture(this.font.deriveFont(3), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
    }

    private void drawLine(double x, double y, double x1, double y1, float width) {
        GL11.glDisable(3553);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glEnable(3553);
    }

    public List<String> wrapWords(String text, double width) {
        ArrayList<String> finalWords = new ArrayList<String>();
        if ((double)this.getStringWidth(text) > width) {
            String[] words = text.split(" ");
            String currentWord = "";
            int lastColorCode = 65535;
            for (String word : words) {
                for (int i = 0; i < word.toCharArray().length; ++i) {
                    char c = word.toCharArray()[i];
                    if (c != this.COLOR_CODE_START || i >= word.toCharArray().length - 1) continue;
                    lastColorCode = word.toCharArray()[i + 1];
                }
                StringBuilder stringBuilder = new StringBuilder();
                if ((double)this.getStringWidth(stringBuilder.append(currentWord).append(word).append(" ").toString()) < width) {
                    currentWord = currentWord + word + " ";
                    continue;
                }
                finalWords.add(currentWord);
                currentWord = this.COLOR_CODE_START + lastColorCode + word + " ";
            }
            if (currentWord.length() > 0) {
                if ((double)this.getStringWidth(currentWord) < width) {
                    finalWords.add(this.COLOR_CODE_START + lastColorCode + currentWord + " ");
                    currentWord = "";
                } else {
                    for (String s : this.formatString(currentWord, width)) {
                        finalWords.add(s);
                    }
                }
            }
        } else {
            finalWords.add(text);
        }
        return finalWords;
    }

    public List<String> formatString(String string, double width) {
        ArrayList<String> finalWords = new ArrayList<String>();
        String currentWord = "";
        int lastColorCode = 65535;
        char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; ++i) {
            char c = chars[i];
            if (c == this.COLOR_CODE_START && i < chars.length - 1) {
                lastColorCode = chars[i + 1];
            }
            StringBuilder stringBuilder = new StringBuilder();
            if ((double)this.getStringWidth(stringBuilder.append(currentWord).append(c).toString()) < width) {
                currentWord = currentWord + c;
                continue;
            }
            finalWords.add(currentWord);
            currentWord = this.COLOR_CODE_START + lastColorCode + String.valueOf(c);
        }
        if (currentWord.length() > 0) {
            finalWords.add(currentWord);
        }
        return finalWords;
    }

    private void setupMinecraftColorCodes() {
        for (int index = 0; index < 32; ++index) {
            int alpha = (index >> 3 & 1) * 85;
            int red = (index >> 2 & 1) * 170 + alpha;
            int green = (index >> 1 & 1) * 170 + alpha;
            int blue = (index & 1) * 170 + alpha;
            if (index == 6) {
                red += 85;
            }
            if (index >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }
            this.colorCode[index] = (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
        }
    }
}
